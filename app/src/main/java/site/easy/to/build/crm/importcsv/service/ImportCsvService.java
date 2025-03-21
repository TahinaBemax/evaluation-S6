/*
package site.easy.to.build.crm.importcsv.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.importcsv.ImportCsvDTO;
import site.easy.to.build.crm.importcsv.ImportCsvServiceDependence;
import site.easy.to.build.crm.importcsv.exception.ImportCsvException;
import site.easy.to.build.crm.importcsv.exception.TableNameNotFoundException;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImportCsvService {
    private final Validator validator;

    @Autowired
    public ImportCsvService(ImportCsvServiceDependence dependence) {
        this.validator = dependence.getValidator();
    }

    private <T> List<T> parseCSVFile(MultipartFile file, Class<T> type, String separator) throws IOException {
        if (separator == null)
            separator = ";";

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(separator.charAt(0))
                    .build();

            return csvToBean.parse();
        }
    }
    private <T> List<T> validateDataCsv(List<T> list) throws ImportCsvException {
        int ligne = 1;
        List<T> valides = new ArrayList<>();

        for (T t : list) {
            Set<ConstraintViolation<T>> violations = validator.validate(t);
            if (!violations.isEmpty()) {
                List<String> messages = new ArrayList<>();
                for (ConstraintViolation<T> violation : violations) {
                    messages.add(violation.getMessage());
                }

                throw new ImportCsvException(messages, ligne);
            }

            valides.add(t);
            ligne++;
        }

        return valides;
    }


    private List<Option> prepareOptionData(MultipartFile file, String separator) throws IOException, ImportCsvException {
        List<OptionCSV> options =  parseCSVFile(file, OptionCSV.class, separator);

        List<OptionCSV> optionCSVList = this.validateDataCsv(options);
        Set<Option> optionSet = new HashSet<>();

        for (OptionCSV optionCSV : optionCSVList) {
            Option temporaire = new Option();

            temporaire.setId(optionCSV.getCode());
            temporaire.setOption(optionCSV.getOption());
            temporaire.setPrix(new BigDecimal(optionCSV.getPrix()));

            optionSet.add(temporaire);
        }

        return optionSet.stream().toList();
    }
    private List<Espace> prepareEspaceData(MultipartFile file, String separator) throws IOException, ImportCsvException {
        List<EspaceCSV> espaces =  parseCSVFile(file, EspaceCSV.class, separator);

        List<EspaceCSV> espaceCSVList = this.validateDataCsv(espaces);
        Set<Espace> espaceSet = new HashSet<>();

        for (EspaceCSV espace : espaceCSVList) {
            Espace temporaire = new Espace();

            temporaire.setNom(espace.getNom());
            temporaire.setPrix(new BigDecimal(espace.getPrix()));

            espaceSet.add(temporaire);
        }

        return espaceSet.stream().toList();
    }
    private List<Reservation> prepareReservationData(MultipartFile file, String separator) throws IOException, ImportCsvException {
        List<Client> clients = this.clientRepository.findAll();
        List<ReservationCSV> reservationCSV =  parseCSVFile(file, ReservationCSV.class, separator);
        List<ReservationCSV> reservationCSVList = this.validateDataCsv(reservationCSV);

        List<Reservation> reservations = new ArrayList<>();

        for (ReservationCSV espace : reservationCSVList) {
            Reservation temporaire = new Reservation();

            temporaire.setReference(espace.getRef());
            temporaire.setDateReservation(espace.getDateReservation());
            temporaire.setHeureDebut(espace.getHeureDebut());
            temporaire.setHeureFin(espace.getHeureDebut().plusHours(espace.getDuree()));
            temporaire.setClient(this.extractClientFromReservationCSV(clients, espace));
            temporaire.setEspace(this.extractEspaceFromReservationCSV(espace));
            temporaire.setPrixEspace(temporaire.getEspace().getPrix());
            temporaire.setOptions(this.extractOptionFromReservationCSV(espace));

            reservations.add(temporaire);
        }

        return reservations;
    }

    private List<Paiement> preparePaiementData(MultipartFile file, String separator) throws IOException, ImportCsvException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Reservation> reservations = this.reservationRepository.findAll();
        List<PaiementCSV> paiementCSV =  parseCSVFile(file, PaiementCSV.class, separator);
        paiementCSV = this.validateDataCsv(paiementCSV);

        List<Paiement> paiements = new ArrayList<>();

        for (PaiementCSV paiement : paiementCSV) {
            Paiement temporaire = new Paiement();

            temporaire.setReservation(
                    reservations.
                            stream().filter(r -> r.getReference().equalsIgnoreCase(paiement.getRef()))
                            .findFirst()
                            .orElseThrow()
            );

            temporaire.setDatePaiement(paiement.getDatePaiement());
            temporaire.setRefPaiement(paiement.getRef_paiement());
            temporaire.setStatutPaiement(PaiementStatut.EN_ATTENTE);
            BigDecimal montantPaye = temporaire.getReservation().getPrixEspace();
            temporaire.setMontantPaye(montantPaye);
            temporaire.setAdminUsername(authentication.getName());

            paiements.add(temporaire);
        }

        return paiements;
    }


    private List<Option> uploadOptionsData(MultipartFile file, String separator) throws IOException, ImportCsvException {
        List<Option> options = this.prepareOptionData(file, separator);
        return this.optionRepository.saveAll(options);
    }
    private List<Espace> uploadEspaceData(MultipartFile file, String separator) throws IOException, ImportCsvException {
        List<Espace> espaces = this.prepareEspaceData(file, separator);
        return this.espaceRepository.saveAll(espaces);
    }
    private List<Paiement> uploadPaiementData(MultipartFile file, String separator) throws ImportCsvException, IOException {
        List<Paiement> Paiements = this.preparePaiementData(file, separator);
        return this.paiementRepository.saveAll(Paiements);
    }
    private List<Reservation> uploadReservationData(MultipartFile file, String separator) throws ImportCsvException, IOException {
        List<Reservation> reservations = this.prepareReservationData(file, separator);
        return this.reservationRepository.saveAll(reservations);
    }

    @Transactional(rollbackOn = {IOException.class, ImportCsvException.class})
    public List<?> uploadCsvData(ImportCsvDTO info) throws ImportCsvException, IOException, TableNameNotFoundException {
        return switch (info.getTableName().toLowerCase()) {
            case "option" -> this.uploadOptionsData(info.getFile(), info.getSeparator());
            case "espace" -> this.uploadEspaceData(info.getFile(), info.getSeparator());
            case "reservation" -> this.uploadReservationData(info.getFile(), info.getSeparator());
            case "paiement" -> this.uploadPaiementData(info.getFile(), info.getSeparator());
            default -> throw new TableNameNotFoundException(info.getTableName());
        };
    }


    private Client extractClientFromReservationCSV(List<Client> clients, ReservationCSV reservationCSV){
        if(clients.stream().noneMatch(c -> c.getNumeroTel().equals(reservationCSV.getNumeroTel()))){
            Client c = new Client();
            c.setNumeroTel(reservationCSV.getNumeroTel());
            c =  clientRepository.save(c);
            clients.add(c);
            return c;
        }

        return clients
                .stream()
                .filter(c -> c.getNumeroTel().equals(reservationCSV.getNumeroTel()))
                .findFirst()
                .orElseThrow();
    }
    private Set<Option> extractOptionFromReservationCSV(ReservationCSV reservationCSV){
        List<Option> options = this.optionRepository.findAll();
        Set<Option> filtredOptions = new HashSet<>();

        @NotNull(message = "{champ.notNull}") List<String> codes = reservationCSV.getOption();

        for (Option option : options) {
            for (String code : codes) {
                if (option.getId().equalsIgnoreCase(code))
                    filtredOptions.add(option);
            }
        }

        return filtredOptions;
    }
    private Espace extractEspaceFromReservationCSV(ReservationCSV reservationCSV){
        List<Espace> espaces = this.espaceRepository.findAll();

        for (Espace espace : espaces) {
            if (espace.getNom().equalsIgnoreCase(reservationCSV.getEspaceName()))
                return espace;
        }

        return null;
    }
}
*/
