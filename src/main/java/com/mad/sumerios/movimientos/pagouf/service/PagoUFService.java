package com.mad.sumerios.movimientos.pagouf.service;

import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.administracion.service.AdministracionService;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.consorcio.service.ConsorcioService;
import com.mad.sumerios.emailsender.EmailSender;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioDTO;
import com.mad.sumerios.estadocuentaconsorcio.model.EstadoCuentaConsorcio;
import com.mad.sumerios.estadocuentaconsorcio.service.EstadoCuentaConsorcioService;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.estadocuentauf.service.EstadoCuentaUfService;
import com.mad.sumerios.expensa.model.Expensa;
import com.mad.sumerios.expensa.repository.IExpensaRepository;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFCreateDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFRequest;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.movimientos.pagouf.repository.IPagoUFRepository;
import com.mad.sumerios.pdf.PdfGenerator2;
import com.mad.sumerios.printPdf.PrintPdf;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import com.mad.sumerios.unidadfuncional.service.UnidadFuncionalService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagoUFService {

    private final IPagoUFRepository pagoUFRepository;
    private final IUnidadFuncionalRepository ufRepository;
    private final AdministracionService administracionService;
    private final IConsorcioRepository consorcioRepository;
    private final ConsorcioService consorcioService;
    private final IExpensaRepository expensaRepository;
    private final EstadoCuentaConsorcioService estadoCuentaConsorcioService;
    private final EstadoCuentaUfService estadoCuentaUfService;
    private final UnidadFuncionalService unidadFuncionalService;
    private final EmailSender emailSender;

    @Autowired
    public PagoUFService(IPagoUFRepository ingresoRepository,
                         IUnidadFuncionalRepository ufRepository,
                         AdministracionService administracionService,
                         IConsorcioRepository consorcioRepository,
                         ConsorcioService consorcioService,
                         IExpensaRepository expensaRepository,
                         EstadoCuentaConsorcioService estadoCuentaConsorcioService,
                         EstadoCuentaUfService estadoCuentaUfService,
                         UnidadFuncionalService unidadFuncionalService,
                         EmailSender emailSender) {
        this.pagoUFRepository  = ingresoRepository;
        this.ufRepository = ufRepository;
        this.administracionService = administracionService;
        this.consorcioRepository = consorcioRepository;
        this.consorcioService = consorcioService;
        this.expensaRepository = expensaRepository;
        this.estadoCuentaConsorcioService = estadoCuentaConsorcioService;
        this.estadoCuentaUfService = estadoCuentaUfService;
        this.unidadFuncionalService = unidadFuncionalService;
        this.emailSender = emailSender;
    }

    //  CREAR INGRESO
    @Transactional
    public void createPagoUF(PagoUFRequest request) throws Exception {
        // Mapeo del DTO a la entidad PagoUF
        PagoUF pago = mapToPagoUFEntity(request.getPago());

        // Actualizar estado de cuenta del consorcio
        EstadoCuentaConsorcioDTO estadoCuentaConsorcio = estadoCuentaConsorcioService.mapToEstadoCuentaDTO(obtenerEstadoCuentaConsorcio(pago.getIdConsorcio()));
        estadoCuentaConsorcioService.sumarPagoUF(estadoCuentaConsorcio, pago);
        // Actualizar estado de cuenta de la unidad funcional
        EstadoCuentaUfDTO estadoCuentaUf = estadoCuentaUfService.mapToEstadoCuentaUfDTO(obtenerEstadoCuentaUf(pago.getIdUf()));
        estadoCuentaUfService.restarPago(estadoCuentaUf, pago);
        // Guardar el pago
        pagoUFRepository.save(pago);

        // dtos para mail y pdf
        UnidadFuncionalResponseDTO ufDto = unidadFuncionalService.getUnidadFuncionalById(pago.getIdUf());
        ConsorcioResponseDTO consorcioDto = consorcioService.getConsorcioById(pago.getIdConsorcio());
        AdministracionResponseDTO admDto = administracionService.getAdministracionById(consorcioDto.getIdAdm());

        Double totalPago = obtenerTotalPagoPeriodo(ufDto.getIdUf(), pago.getPeriodo()) - pago.getValor();

        // Generar el PDF
        Path tempFile = Files.createTempFile("pago_uf_" + ufDto.getUnidadFuncional(), ".pdf");
        String outputPath = tempFile.toAbsolutePath().toString();

        PdfGenerator2.createPdfPago(this.mapToPagoUFDTO(pago),totalPago ,admDto, consorcioDto, ufDto, outputPath);

        // Verificar archivo generado
        File pdfFile = tempFile.toFile();
        if (pdfFile.exists()) {
            System.out.println("PDF generado en: " + pdfFile.getAbsolutePath());
            System.out.println("Tamaño del PDF: " + pdfFile.length() + " bytes");
        }

        // Imprimir pdf
        if(request.getImprimir()){
            PrintPdf.printPdf(outputPath);
        }
        // Enviar el PDF por correo
        if(request.getEnviarMail()){
            emailSender.enviarPagoPorCorreo(request.getMails(),ufDto, consorcioDto.getNombre(), outputPath);
        }


        // Eliminar el archivo temporal
        new File(outputPath).delete();
    }

    private Double obtenerTotalPagoPeriodo(long idUf, YearMonth periodo) {
        double totalPago = 0;
        List<PagoUFDTO> pagos = this.getPagoUFByUnidadFuncionalAndPeriodo(idUf, periodo);
        for(PagoUFDTO pago : pagos){
            totalPago += pago.getValor();
        }
        return totalPago;
    }

    //  LISTAR INGRESO
    //  por unidad funcional
    public List<PagoUFDTO> getPagoUFByUnidadFuncional(Long idUf){
        List<PagoUF> pagos = pagoUFRepository.findByidUf(idUf);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    public List<PagoUFDTO> getPagoUFByUnidadFuncionalAndPeriodo(Long idUf, YearMonth periodo) {
        List<PagoUF> pagos = pagoUFRepository.findByidUfAndPeriodo(idUf,periodo);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por unidad funcional y fechas
    public List<PagoUFDTO> getPagosByUnidadFuncionalAndFecha(Long unidadFuncionalId, LocalDate startDate, LocalDate endDate) throws Exception {
        if(ufRepository.findById(unidadFuncionalId).isEmpty()){
            throw new Exception("Unidad Funcional no encontrada.");
        }

        List<PagoUF> pagos = pagoUFRepository.findByidUfAndFechaBetween(unidadFuncionalId, startDate, endDate);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por consorcio
    public List<PagoUFDTO> getPagoUFByConsorcio(Long idConsorcio){
        List<PagoUF> pagos = pagoUFRepository.findByIdConsorcio(idConsorcio);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por consorcio y periodo
    public List<PagoUFDTO> getPagoUFByPeriodoAndConsorcio(YearMonth periodo, Long idConsorcio){
        List<PagoUF> pagos = pagoUFRepository.findByPeriodoAndIdConsorcio(periodo, idConsorcio);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por consorcio y fechas
    public List<PagoUFDTO> getPagoUFByConsorcioAndFecha(Long idConsorcio, LocalDate startDate, LocalDate endDate){
        List<PagoUF> pagos = pagoUFRepository.findByIdConsorcioAndFechaBetween(idConsorcio, startDate, endDate);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por consorcio y forma de pago
    public List<PagoUFDTO> getPagoUFByConsorcioAndFormaPago(Long idConsorcio, FormaPago formaPago){
        List<PagoUF> pagos = pagoUFRepository.findByIdConsorcioAndFormaPago(idConsorcio, formaPago);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  forma de pago
    public List<PagoUFDTO> getPagoUFByFormaPago(FormaPago formaPago){
        List<PagoUF> pagos = pagoUFRepository.findByFormaPago(formaPago);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    // buscar por expensa
    public List<PagoUFDTO> getPagoUFByExpensa(Long idConsorcio, YearMonth periodo) {
        Expensa exp = expensaRepository.findByidConsorcioAndPeriodo(idConsorcio, periodo);
        List<PagoUF> pagos= pagoUFRepository.findByExpensa_idExpensa(exp.getIdExpensa());
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }

    //  ACTUALIZAR INGRESO
//    public void updatePagoUF(Long idPagoUf, PagoUFUpdateDTO dto) throws Exception{
//        PagoUF pago = pagoUFRepository.findById(idPagoUf)
//                .orElseThrow(()-> new Exception("Pago no encontrado"));
//        PagoUF pagoUpdated = mapToPagoUFEntityUpdate(dto);
//
//        EstadoCuentaConsorcio estadoCuentaConsorcio = obtenerEstadoCuentaConsorcio(pago.getIdConsorcio());
//        estadoCuentaConsorcioService.modificarPagoUF(estadoCuentaConsorcio, pago, pagoUpdated);
//
//        pago.setFecha(pagoUpdated.getFecha());
//        pago.setValor(pagoUpdated.getValor());
//        pago.setDescripcion(pagoUpdated.getDescripcion());
//        pago.setFormaPago(pagoUpdated.getFormaPago());
//
//        pagoUFRepository.save(pago);
//    }

    //  ELIMINAR INGRESO
    public void deletePagoUF(Long id) throws Exception{
        PagoUF pago = pagoUFRepository.findById(id)
                .orElseThrow(()-> new Exception("Pago UF no encontrado"));

        EstadoCuentaConsorcio estadoCuentaConsorcio = obtenerEstadoCuentaConsorcio(pago.getIdConsorcio());
        estadoCuentaConsorcioService.revertirPagoUF(estadoCuentaConsorcio, pago);


        EstadoCuentaUf estadoCuentaUf = obtenerEstadoCuentaUf(pago.getIdUf());
        estadoCuentaUfService.revertirPago(estadoCuentaUf, pago);

        pagoUFRepository.delete(pago);
    }

    //validaciones y aux
    private void validateNull(Object object) throws Exception{
        if(object == null){
            throw new Exception("Pago nulo");
        }
    }
    private void validateConsorcio(Long idConsorcio) throws Exception{
        if(consorcioRepository.findById(idConsorcio).isEmpty()) {
            throw new Exception("Consorcio no encontrado.");
        }
    }
    private void validateUf(Long idUf) throws Exception{
        ufRepository.findById(idUf)
                .orElseThrow(()-> new Exception("Pago UF no encontrado"));
    }
    private void validateValor(Double valor) throws Exception{
        if (valor <= 0){
            throw new Exception("El pago debe ser mayor a $0");
        }
    }
    private Expensa validateExpensa(Long idExpensa) throws Exception {
        Optional<Expensa> exp = expensaRepository.findById(idExpensa);
        if(exp.isEmpty()){
            throw new Exception("Expensa no encontrado");
        }

        return exp.get();
    }
    private EstadoCuentaConsorcio obtenerEstadoCuentaConsorcio(Long idConsorcio) throws Exception {
        Consorcio consorcio = consorcioRepository.findById(idConsorcio)
                .orElseThrow(() -> new Exception("Consorcio no encontrado."));

        return Optional.ofNullable(consorcio.getEstadoCuentaConsorcio())
                .orElseThrow(() -> new Exception("Estado de cuenta del consorcio no encontrado."));
    }
    private EstadoCuentaUf obtenerEstadoCuentaUf(Long idUf) throws Exception {
        UnidadFuncional unidadFuncional = ufRepository.findById(idUf)
                .orElseThrow(() -> new Exception("Unidad Funcional no encontrada."));

        return Optional.ofNullable(unidadFuncional.getEstadoCuentaUf())
                .orElseThrow(() -> new Exception("Estado de cuenta no encontrado para la Unidad Funcional."));
    }

    // mapeo DTO a Entity
    private PagoUF mapToPagoUFEntity(PagoUFCreateDTO dto) throws Exception {
        validateNull(dto);
        validateUf(dto.getIdUf());
        validateConsorcio(dto.getIdConsorcio());
        Expensa exp = validateExpensa(dto.getIdExpensa());
        PagoUF pagoUF = new PagoUF();

        pagoUF.setIdUf(dto.getIdUf());
        pagoUF.setIdConsorcio(dto.getIdConsorcio());
        pagoUF.setFecha(dto.getFecha());
        pagoUF.setValor(dto.getValor());
        pagoUF.setFormaPago(dto.getFormaPago());
        pagoUF.setDescripcion(dto.getDescripcion());
        pagoUF.setExpensa(exp);
        pagoUF.setPeriodo(dto.getPeriodo());

        return pagoUF;
    }
//    private PagoUF mapToPagoUFEntityUpdate(PagoUFUpdateDTO dto) throws Exception {
//        validateNull(dto);
//        validateValor(dto.getValor());
//
//        PagoUF pagoUF = new PagoUF();
//
//        pagoUF.setFecha(dto.getFecha());
//        pagoUF.setValor(dto.getValor());
//        pagoUF.setFormaPago(dto.getFormaPago());
//        pagoUF.setDescripcion(dto.getDescripcion());
//
//        return pagoUF;
//    }

    // mapeo Entity a DTO
    public PagoUFDTO mapToPagoUFDTO(PagoUF pagoUF) {
        PagoUFDTO dto = new PagoUFDTO();

        dto.setIdPagoUF(pagoUF.getIdPagoUF());
        dto.setIdUf(pagoUF.getIdUf());
        dto.setFecha(pagoUF.getFecha());
        dto.setValor(pagoUF.getValor());
        dto.setFormaPago(pagoUF.getFormaPago());
        dto.setDescripcion(pagoUF.getDescripcion());
        dto.setIdExpensa(pagoUF.getExpensa().getIdExpensa());
        dto.setPeriodo(pagoUF.getPeriodo());

        return dto;
    }

}
