package com.mad.sumerios.expensa.service;

import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.consorcio.service.ConsorcioService;
import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.estadocuentauf.repository.IEstadoCuentaUfRepository;
import com.mad.sumerios.estadocuentauf.service.EstadoCuentaUfService;
import com.mad.sumerios.expensa.dto.ExpensaCreateDTO;
import com.mad.sumerios.expensa.dto.ExpensaResponseDto;
import com.mad.sumerios.expensa.model.Expensa;
import com.mad.sumerios.expensa.repository.IExpensaRepository;
import com.mad.sumerios.intermedioExpensaConsorcio.dto.IntermediaExpensaConsorcioCreateDto;
import com.mad.sumerios.intermedioExpensaConsorcio.dto.IntermediaExpensaConsorcioDto;
import com.mad.sumerios.intermedioExpensaConsorcio.model.IntermediaExpensaConsorcio;
import com.mad.sumerios.intermedioExpensaConsorcio.repository.IIntermediaExpensaConsorcioRepository;
import com.mad.sumerios.intermedioExpensaConsorcio.service.IntermediaExpensaConsorcioService;
import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.egreso.service.EgresoService;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularResponseDTO;
import com.mad.sumerios.movimientos.gastoParticular.model.GastoParticular;
import com.mad.sumerios.movimientos.gastoParticular.service.GastoParticularService;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoResponseDTO;
import com.mad.sumerios.movimientos.ingreso.model.Ingreso;
import com.mad.sumerios.movimientos.ingreso.service.IngresoService;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.movimientos.pagouf.service.PagoUFService;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import com.mad.sumerios.unidadfuncional.service.UnidadFuncionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpensaService {

    private final IExpensaRepository expensaRepository;
    private final IConsorcioRepository consorcioRepository;
    private final ConsorcioService consorcioService;
    private final IUnidadFuncionalRepository unidadFuncionalRepository;
    private final UnidadFuncionalService unidadFuncionalService;
    private final IEstadoCuentaUfRepository estadoCuentaUfRepository;
    private final EstadoCuentaUfService estadoCuentaUfService;
    private final IIntermediaExpensaConsorcioRepository intermediaExpensaConsorcioRepository;
    private final IntermediaExpensaConsorcioService intermediaExpensaConsorcioService;
    private final EgresoService egresoService;
    private final GastoParticularService gastoParticularService;
    private final IngresoService ingresoService;
    private final PagoUFService pagoUFService;
    @Autowired
    public ExpensaService (IExpensaRepository expensaRepository,
                           IConsorcioRepository consorcioRepository,
                           ConsorcioService consorcioService,
                           IUnidadFuncionalRepository unidadFuncionalRepository,
                           UnidadFuncionalService unidadFuncionalService,
                           EstadoCuentaUfService estadoCuentaUfService,
                           IEstadoCuentaUfRepository estadoCuentaUfRepository,
                           IIntermediaExpensaConsorcioRepository intermediaExpensaConsorcioRepository,
                           IntermediaExpensaConsorcioService intermediaExpensaConsorcioService,
                           EgresoService egresoService,
                           GastoParticularService gastoParticularService,
                           IngresoService ingresoService,
                           PagoUFService pagoUFService){
        this.expensaRepository = expensaRepository;
        this.consorcioRepository = consorcioRepository;
        this.consorcioService = consorcioService;
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.unidadFuncionalService = unidadFuncionalService;
        this.estadoCuentaUfService = estadoCuentaUfService;
        this.estadoCuentaUfRepository = estadoCuentaUfRepository;
        this.intermediaExpensaConsorcioRepository = intermediaExpensaConsorcioRepository;
        this.intermediaExpensaConsorcioService = intermediaExpensaConsorcioService;
        this.egresoService = egresoService;
        this.gastoParticularService = gastoParticularService;
        this.ingresoService = ingresoService;
        this.pagoUFService = pagoUFService;
    }

    @Transactional
    public void liquidarExpensaMesVencido(Long idConsorcio, Long idExpensa, ExpensaCreateDTO dto) throws Exception{
        validateConsorcio(idConsorcio);
        validateUltimoPeriodo(idConsorcio, dto.getPeriodo());
        ExpensaResponseDto expensa = this.getExpensasById(idExpensa);

        List<UnidadFuncionalResponseDTO> ufs = unidadFuncionalService.getUnidadesPorConsorcio(idConsorcio);
        List<EstadoCuentaUfDTO> estadosDeCuentaUf = estadoCuentaUfService.getEstadoCuentaUfs(ufs);

        // CREA COPIA DE ECUF ACTUAL
        createCopiasEstadoDeCuentaUf(estadosDeCuentaUf);
        // ECUF -> TOMA EL SALDO EXPENSAS Y LO TRANSFORMA EN DEUDA Y CALCULA EL INTERES
        aplicarDeudaEIntereses(estadosDeCuentaUf, expensa.getPorcentajeIntereses());

    // SUMA GASTOS A, B, C, D Y E
        double totalA = totalGastos(expensa.getEgresos(),CategoriaEgreso.A);
        double totalB = totalGastos(expensa.getEgresos(),CategoriaEgreso.B);
        double totalC = totalGastos(expensa.getEgresos(),CategoriaEgreso.C);
        double totalD = totalGastos(expensa.getEgresos(),CategoriaEgreso.D);
        double totalE = totalGastos(expensa.getEgresos(),CategoriaEgreso.E);

    // ECUF -> APLICA A ESTADO DE CUENTA UF POR PORCENTAJE SUMATORIA DE CADA GASTO

        if (totalA > 0) {
            aplicarTotal(estadosDeCuentaUf, totalA, CategoriaEgreso.A);
        }
        if (totalB > 0) {
            aplicarTotal(estadosDeCuentaUf, totalB, CategoriaEgreso.B);
        }
        if (totalC > 0) {
            aplicarTotal(estadosDeCuentaUf, totalC, CategoriaEgreso.C);
        }
        if (totalD > 0) {
            aplicarTotal(estadosDeCuentaUf, totalD, CategoriaEgreso.D);
        }
        if (totalE > 0) {
            aplicarTotal(estadosDeCuentaUf, totalE, CategoriaEgreso.E);
        }

    // ECUF -> APLICA VALOR POR GASTOS PARTICULARES O APLICA 0
        aplicarGastosParticulares(expensa.getGp());
    // ECUF -> SUMA DEUDA, INTERES, GASTOS PARTICULARES Y TOTALES Y DA EL TOTAL FINAL, APLICA EL SALDO EXPENSA CON LA SUMA DE LOS TOTALES
        aplicarValorTotal(estadosDeCuentaUf);
    // CREA NUEVA EXPENSA  VACIA E INTNERMEDIA
        createExpensa(dto);
    }

    @Transactional
    public void createExpensa(ExpensaCreateDTO dto) throws Exception {
        expensaRepository.save(mapToExpensaEntity(dto));

        // CREA ENTIDAD INTERMEDIA PARA SABER LA ULTIMA EXPENSA LIQUIDADA DEL CONSORCIO
        IntermediaExpensaConsorcio intermedia = intermediaExpensaConsorcioRepository.findByIdConsorcio(dto.getIdConsorcio());

        if(intermedia == null){
            ExpensaResponseDto expIntermediaNueva = this.getExpensasByConsorcioAndPeriodo(dto.getIdConsorcio(), dto.getPeriodo());

            IntermediaExpensaConsorcioCreateDto dtoIntermedia = new IntermediaExpensaConsorcioCreateDto();
            dtoIntermedia.setIdConsorcio(dto.getIdConsorcio());
            dtoIntermedia.setIdExpensa(expIntermediaNueva.getIdExpensa());
            dtoIntermedia.setPeriodo(expIntermediaNueva.getPeriodo());
            intermediaExpensaConsorcioService.createIntermediaExpensaConsorcio(dtoIntermedia);
        } else {
            ExpensaResponseDto expIntermediaUpdate = this.getExpensasByConsorcioAndPeriodo(dto.getIdConsorcio(), dto.getPeriodo().plusMonths(1));

            IntermediaExpensaConsorcioDto dtoIntermediaUpdate = new IntermediaExpensaConsorcioDto();
            dtoIntermediaUpdate.setIdIntermedia(intermedia.getIdIntermedia());
            dtoIntermediaUpdate.setIdExpensa(expIntermediaUpdate.getIdExpensa());
            dtoIntermediaUpdate.setIdConsorcio(intermedia.getIdConsorcio());
            dtoIntermediaUpdate.setPeriodo(expIntermediaUpdate.getPeriodo());
            intermediaExpensaConsorcioService.updateIntermediaExpensaConsorcio(dtoIntermediaUpdate);
        }

    }

    @Transactional
    public ExpensaResponseDto restablecerPeriodo(Long idExpensa) throws Exception {
        ExpensaResponseDto expensaUltima = this.getExpensasById(idExpensa);
        System.out.println("1- Expensa ultima"+ expensaUltima);
        ExpensaResponseDto expensaAnterior = this.getExpensasByConsorcioAndPeriodo(expensaUltima.getIdConsorcio(), expensaUltima.getPeriodo().minusMonths(1));
        System.out.println("2- Expensa anterior"+ expensaAnterior);

        if(expensaAnterior == null){
            throw new Exception("No existe expensa previa");
        }

        // ELIMINA LOS MOVIMIENTOS
        for(EgresoResponseDTO egreso : expensaUltima.getEgresos()){
            egresoService.deleteEgreso(egreso.getIdEgreso());
        }
        for(IngresoResponseDTO ingreso : expensaUltima.getIngresos()){
            ingresoService.deleteIngreso(ingreso.getIdIngreso());
        }
        for(GastoParticularResponseDTO gp : expensaUltima.getGp()){
            gastoParticularService.deleteGastoParticular(gp.getIdGastoParticular());
        }
        for(PagoUFDTO pago : expensaUltima.getPagoUf()){
            pagoUFService.deletePagoUF(pago.getIdPagoUF());
        }

        // REESTABLE LOS ESTADOS DE CUENTA
        List<EstadoCuentaUfDTO> estadosDeCuentaUf = estadoCuentaUfService.getEstadoCuentaUfs(
                unidadFuncionalService.getUnidadesPorConsorcio(expensaUltima.getIdConsorcio()));

        for (EstadoCuentaUfDTO ecuf : estadosDeCuentaUf){
            estadoCuentaUfService.restablecerPeriodoPrevio(ecuf.getIdEstadoCuentaUf());
        }

        // ACTUALIZA CLASE INTERMEDIA
        IntermediaExpensaConsorcioDto intermedia = intermediaExpensaConsorcioService.getIntermediaByConsorcio(expensaUltima.getIdConsorcio());
        System.out.println("9 INTERMEDIA ANTES DE ACTUALIZAR: "+ intermedia);

        intermedia.setIdExpensa(expensaAnterior.getIdExpensa());
        intermedia.setPeriodo(expensaAnterior.getPeriodo());
        intermediaExpensaConsorcioService.updateIntermediaExpensaConsorcio(intermedia);
        System.out.println("10 INTERMEDIA DESPUES DE ACTUALIZAR: "+ intermedia);

        expensaRepository.deleteById(idExpensa);

        return expensaAnterior;
    }
    // TODAS LAS EXPENSA
    public List<ExpensaResponseDto> getExpensas(){
        List<Expensa> expensas = expensaRepository.findAll();
        return expensas.stream().map(this::mapToExpensaResponse).collect(Collectors.toList());
    }
    // POR ID
    public ExpensaResponseDto getExpensasById(Long idExpensa) throws Exception {
        Expensa expensa = expensaRepository.findById(idExpensa)
                .orElseThrow(()-> new Exception("Expensa no encontrada"));
        return mapToExpensaResponse(expensa);
    }
    // POR CONSORCIO
    public List<ExpensaResponseDto> getExpensasByConsorcio(Long idConsorcio){
        List<Expensa> expensas = expensaRepository.findByidConsorcio(idConsorcio);
        return expensas.stream().map(this::mapToExpensaResponse).collect(Collectors.toList());
    }
    // POR CONSORCIO Y PERIODO
    public ExpensaResponseDto getExpensasByConsorcioAndPeriodo(Long idConsorcio, YearMonth periodo){
        Expensa expensa = expensaRepository.findByidConsorcioAndPeriodo(idConsorcio, periodo);
        return mapToExpensaResponse(expensa);
    }

    // BORRAR EXPENSA

    // Validates y metodos complementarios
    private void validateConsorcio(Long idConsorcio) throws Exception{
        consorcioRepository.findById(idConsorcio)
                .orElseThrow(()-> new Exception("Consorcio no encontrado"));
    }
    private void validatePeriodo(Long idConsorcio, YearMonth periodo) throws Exception {
        Expensa expensa = expensaRepository.findByidConsorcioAndPeriodo(idConsorcio, periodo);
        if(expensa != null){
            throw new Exception("El período "+ periodo + " ya existe en el consorcio.");
        }
    }
    private void validateUltimoPeriodo(Long idConsorcio, YearMonth periodo) throws Exception{
        Expensa expensa = expensaRepository.findByidConsorcioAndPeriodo(idConsorcio, periodo.plusMonths(1));
        if(expensa != null){
            throw new Exception("El período "+ periodo + " no es el último en el consorcio.");
        }
    }
    private void validatePorcentajeIntereses(Double porcentajeIntereses) throws Exception{
        if (porcentajeIntereses < 0){
            throw new Exception("El porcentaje de intereses debe ser igual o mayor a 0%");
        }
    }
    private void createCopiasEstadoDeCuentaUf(List<EstadoCuentaUfDTO> estadosDeCuentaUf) throws Exception {
        for (EstadoCuentaUfDTO estadoActual : estadosDeCuentaUf) {
            estadoCuentaUfService.createCopiaEstadoCuentaUf(estadoActual);
        }
    }
    private double totalGastos(List<EgresoResponseDTO> egresos, CategoriaEgreso categoriaEgreso) {
        if (egresos == null || egresos.isEmpty() || categoriaEgreso == null) {
            return 0.0;
        }

        return egresos.stream()
                .filter(egreso -> categoriaEgreso.equals(egreso.getCategoriaEgreso()))
                .mapToDouble(EgresoResponseDTO::getTotalFinal)
                .sum();
    }
    private void aplicarTotal(List<EstadoCuentaUfDTO> estadoCuentaUfs,
                              double total,
                              CategoriaEgreso categoria) throws Exception {
        for (EstadoCuentaUfDTO estadoActual : estadoCuentaUfs) {
            UnidadFuncionalResponseDTO uf = unidadFuncionalService.getUnidadFuncionalById(estadoActual.getIdUf());

            estadoCuentaUfService.aplicarValorCategoria(estadoActual.getIdEstadoCuentaUf(),
                                                        uf.getPorcentajeUnidad(),
                                                        total,
                                                        categoria);

        }
    }
    private void aplicarGastosParticulares(List<GastoParticularResponseDTO> gastosParticulares) throws Exception {
        for(GastoParticularResponseDTO gp : gastosParticulares){
            double valor = gp.getTotalFinal();
            if(valor>0){
                UnidadFuncional uf = unidadFuncionalRepository.findById(gp.getIdUf())
                        .orElseThrow(()->new Exception("Unidad funcional no encontrada."));
                estadoCuentaUfService.aplicarValorGastoParticular(uf.getEstadoCuentaUf().getIdEstadoCuentaUf(), valor);
            }
        }
    }
    private void aplicarDeudaEIntereses(List<EstadoCuentaUfDTO> estadosDeCuentaUf, double porcentajeIntereses) throws Exception {
        for (EstadoCuentaUfDTO estadoActual : estadosDeCuentaUf) {
            if(estadoActual.getTotalFinal() > 0){
                estadoCuentaUfService.aplicarDeudaEIntereses(estadoActual.getIdEstadoCuentaUf(), porcentajeIntereses);
            } else {
                EstadoCuentaUf ecuf = estadoCuentaUfRepository.findById(estadoActual.getIdEstadoCuentaUf())
                                .orElseThrow(()-> new Exception("Estado de cuenta no encontrado"));

                ecuf.setDeuda(ecuf.getSaldoExpensa());
                ecuf.setIntereses(0.0);
                ecuf.setTotalFinal(0.0);
                ecuf.setSaldoExpensa(0.0);
                ecuf.setSaldoIntereses(0.0);
            }
        }
    }
    private void aplicarValorTotal(List<EstadoCuentaUfDTO> estadosDeCuentaUf) throws Exception {
        for (EstadoCuentaUfDTO estadoActual : estadosDeCuentaUf) {
            estadoCuentaUfService.aplicarTotalFinal(estadoActual.getIdEstadoCuentaUf());
        }
    }
    // Mapeo a entidad
    private Expensa mapToExpensaEntity(ExpensaCreateDTO dto) throws Exception {
        validateConsorcio(dto.getIdConsorcio());
        validatePorcentajeIntereses(dto.getPorcentajeIntereses());
        Expensa expensa = new Expensa();

        expensa.setIdConsorcio(dto.getIdConsorcio());
        if(expensaRepository.findByidConsorcio(dto.getIdConsorcio()).isEmpty()){
            expensa.setPeriodo(dto.getPeriodo());
        } else {
            expensa.setPeriodo(dto.getPeriodo().plusMonths(1));
        }

        validatePeriodo(dto.getIdConsorcio(), expensa.getPeriodo());
        expensa.setPorcentajeIntereses(dto.getPorcentajeIntereses());
        expensa.setPorcentajeSegundoVencimiento(dto.getPorcentajeSegundoVencimiento());
        return expensa;
    }

    private ExpensaResponseDto mapToExpensaResponse (Expensa expensa) {
        ExpensaResponseDto dto = new ExpensaResponseDto();

        dto.setIdExpensa(expensa.getIdExpensa());
        dto.setIdConsorcio(expensa.getIdConsorcio());
        dto.setPeriodo(expensa.getPeriodo());
        dto.setPorcentajeIntereses(expensa.getPorcentajeIntereses());
        dto.setPorcentajeSegundoVencimiento(expensa.getPorcentajeSegundoVencimiento());

        if(expensa.getEgresos() != null){
            List<Egreso> egresos = expensa.getEgresos();
            for(Egreso egreso : egresos){
                dto.getEgresos().add(egresoService.mapToEgresoResponse(egreso));
            }
        }

        if(expensa.getGastosParticulares() != null){
            List<GastoParticular> gps = expensa.getGastosParticulares();
            for(GastoParticular gp : gps){
                dto.getGp().add(gastoParticularService.mapToGastoParticularResponse(gp));
            }
        }

        if(expensa.getIngresos() != null){
            List<Ingreso> ingresos = expensa.getIngresos();
            for(Ingreso ingreso : ingresos){
                dto.getIngresos().add(ingresoService.mapToIngresoResponseDTO(ingreso));
            }
        }


        if(expensa.getPagoUFS() != null){
            List<PagoUF> pagoUfs = expensa.getPagoUFS();
            for(PagoUF pagoUf : pagoUfs){
                dto.getPagoUf().add(pagoUFService.mapToPagoUFDTO(pagoUf));
            }
        }

        return dto;
    }
}
