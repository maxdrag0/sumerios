package com.mad.sumerios.expensa.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.enums.CategoriaEgreso;
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
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.egreso.service.EgresoService;
import com.mad.sumerios.movimientos.gastoParticular.model.GastoParticular;
import com.mad.sumerios.movimientos.gastoParticular.service.GastoParticularService;
import com.mad.sumerios.movimientos.ingreso.model.Ingreso;
import com.mad.sumerios.movimientos.ingreso.service.IngresoService;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.movimientos.pagouf.service.PagoUFService;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
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
    private final IUnidadFuncionalRepository unidadFuncionalRepository;
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
                           IUnidadFuncionalRepository unidadFuncionalRepository,
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
        this.unidadFuncionalRepository = unidadFuncionalRepository;
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
    public void liquidarExpensaMesVencido(Long idExpensa, Long idConsorcio, ExpensaCreateDTO dto) throws Exception{
        Expensa expensa = expensaRepository.findById(idExpensa)
                .orElseThrow(()-> new Exception("Expensa no encontrado"));
        Consorcio consorcio = consorcioRepository.findById(idConsorcio)
                .orElseThrow(()-> new Exception("Consorcio no encontrado"));

        List<UnidadFuncional> ufs = consorcio.getUnidadesFuncionales();
        List<EstadoCuentaUf> estadosDeCuentaUf = estadoCuentaUfRepository.findByUnidadFuncionalIn(ufs);

    // CREA COPIA DE ECUF ACTUAL
        createCopiasEstadoDeCuentaUf(estadosDeCuentaUf);
    // SUMA GASTOS A, B, C, D Y E
        double totalA = totalGastos(expensa.getEgresos(),CategoriaEgreso.A);
        double totalB = totalGastos(expensa.getEgresos(),CategoriaEgreso.B);
        double totalC = totalGastos(expensa.getEgresos(),CategoriaEgreso.C);
        double totalD = totalGastos(expensa.getEgresos(),CategoriaEgreso.D);
        double totalE = totalGastos(expensa.getEgresos(),CategoriaEgreso.E);
    // ECUF -> APLICA A ESTADO DE CUENTA UF POR PORCENTAJE SUMATORIA DE CADA GASTO
        aplicarTotal(estadosDeCuentaUf, totalA, CategoriaEgreso.A);
        aplicarTotal(estadosDeCuentaUf, totalB, CategoriaEgreso.B);
        aplicarTotal(estadosDeCuentaUf, totalC, CategoriaEgreso.C);
        aplicarTotal(estadosDeCuentaUf, totalD, CategoriaEgreso.D);
        aplicarTotal(estadosDeCuentaUf, totalE, CategoriaEgreso.E);
    // ECUF -> APLICA VALOR POR GASTOS PARTICULARES O APLICA 0
        aplicarGastosParticulares(expensa.getGastosParticulares());
    // ECUF -> TOMA EL SALDO EXPENSAS Y LO TRANSFORMA EN DEUDA Y CALCULA EL INTERES
        aplicarDeudaEIntereses(estadosDeCuentaUf, expensa.getPorcentajeIntereses());
    // ECUF -> SUMA DEUDA, INTERES, GASTOS PARTICULARES Y TOTALES Y DA EL TOTAL FINAL, APLICA EL SALDO EXPENSA CON LA SUMA DE LOS TOTALES
        aplicarValorTotal(estadosDeCuentaUf);
    // CREA NUEVA EXPENSA  VACIA E INTNERMEDIA
        createExpensa(dto);
    }

    @Transactional
    public void createExpensa(ExpensaCreateDTO dto) throws Exception {
        Expensa exp = mapToExpensaEntity(dto);
        expensaRepository.save(exp);

        // CREA ENTIDAD INTERMEDIA PARA SABER LA ULTIMA EXPENSA LIQUIDADA DEL CONSORCIO
        IntermediaExpensaConsorcio intermedia = intermediaExpensaConsorcioRepository.findByIdConsorcio(dto.getIdConsorcio());

        if(intermedia == null){
            IntermediaExpensaConsorcioCreateDto dtoIntermedia = new IntermediaExpensaConsorcioCreateDto();
            dtoIntermedia.setIdConsorcio(dto.getIdConsorcio());
            dtoIntermedia.setIdExpensa(exp.getIdExpensa());
            dtoIntermedia.setPeriodo(exp.getPeriodo());
            intermediaExpensaConsorcioService.createIntermediaExpensaConsorcio(dtoIntermedia);
        } else {
            IntermediaExpensaConsorcioDto dtoIntermediaUpdate = new IntermediaExpensaConsorcioDto();
            dtoIntermediaUpdate.setIdIntermedia(intermedia.getIdIntermedia());
            dtoIntermediaUpdate.setIdExpensa(intermedia.getIdExpensa());
            dtoIntermediaUpdate.setIdConsorcio(intermedia.getIdConsorcio());
            dtoIntermediaUpdate.setPeriodo(intermedia.getPeriodo());
            intermediaExpensaConsorcioService.updateIntermediaExpensaConsorcio(dtoIntermediaUpdate);
        }

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
        List<Expensa> expensas = expensaRepository.findByConsorcio_idConsorcio(idConsorcio);
        return expensas.stream().map(this::mapToExpensaResponse).collect(Collectors.toList());
    }
    // POR CONSORCIO Y PERIODO
    public ExpensaResponseDto getExpensasByConsorcioAndPeriodo(Long idConsorcio, YearMonth periodo){
        Expensa expensa = expensaRepository.findByConsorcio_idConsorcioAndPeriodo(idConsorcio, periodo);
        return mapToExpensaResponse(expensa);
    }

    // Validates y metodos complementarios
    private Consorcio validateConsorcio(Long idConsorcio) throws Exception{
        return consorcioRepository.findById(idConsorcio)
                .orElseThrow(()-> new Exception("Consorcio no encontrado"));
    }
    private void validatePeriodo(Long idConsorcio, YearMonth periodo) throws Exception {
        Expensa expensa = expensaRepository.findByConsorcio_idConsorcioAndPeriodo(idConsorcio, periodo);
        if(expensa != null){
            throw new Exception("El período "+ periodo + " ya existe en el consorcio.");
        }
    }
    private void validatePorcentajeIntereses(Double porcentajeIntereses) throws Exception{
        if (porcentajeIntereses < 0){
            throw new Exception("El porcentaje de intereses debe ser igual o mayor a 0%");
        }
    }
    private void createCopiasEstadoDeCuentaUf(List<EstadoCuentaUf> estadosDeCuentaUf) throws Exception {
        for (EstadoCuentaUf estadoActual : estadosDeCuentaUf) {
            estadoCuentaUfService.createCopiaEstadoCuentaUf(estadoActual);
        }
    }
    private double totalGastos(List<Egreso> egresos, CategoriaEgreso categoriaEgreso) {
        if (egresos == null || egresos.isEmpty() || categoriaEgreso == null) {
            return 0.0;
        }

        return egresos.stream()
                .filter(egreso -> categoriaEgreso.equals(egreso.getCategoriaEgreso()))
                .mapToDouble(Egreso::getTotalFinal)
                .sum();
    }
    private void aplicarTotal(List<EstadoCuentaUf> estadoCuentaUfs,
                              double total,
                              CategoriaEgreso categoria) throws Exception {
        for (EstadoCuentaUf estadoActual : estadoCuentaUfs) {
            estadoCuentaUfService.aplicarValorCategoria(estadoActual.getIdEstadoCuentaUf(),
                                               estadoActual.getUnidadFuncional().getPorcentajeUnidad(),
                                               total,
                                               categoria);
        }
    }
    private void aplicarGastosParticulares(List<GastoParticular> gastosParticulares) throws Exception {
        for(GastoParticular gp : gastosParticulares){
            double valor = gp.getTotalFinal();
            UnidadFuncional uf = unidadFuncionalRepository.findById(gp.getIdUf())
                    .orElseThrow(()->new Exception("Unidad funcional no encontrada."));
            estadoCuentaUfService.aplicarValorGastoParticular(uf.getEstadoCuentaUf().getIdEstadoCuentaUf(), valor);
        }
    }
    private void aplicarDeudaEIntereses(List<EstadoCuentaUf> estadosDeCuentaUf, double porcentajeIntereses) throws Exception {
        for (EstadoCuentaUf estadoActual : estadosDeCuentaUf) {
            if(estadoActual.getTotalFinal() > 0){
                estadoCuentaUfService.aplicarDeudaEIntereses(estadoActual.getIdEstadoCuentaUf(), porcentajeIntereses);
            } else {
                estadoActual.setDeuda(estadoActual.getSaldoExpensa());
                estadoActual.setIntereses(0.0);
                estadoActual.setSaldoExpensa(0.0);
                estadoActual.setSaldoIntereses(0.0);
            }
        }
    }
    private void aplicarValorTotal(List<EstadoCuentaUf> estadosDeCuentaUf) throws Exception {
        for (EstadoCuentaUf estadoActual : estadosDeCuentaUf) {
            estadoCuentaUfService.aplicarTotalFinal(estadoActual.getIdEstadoCuentaUf());
        }
    }
    // Mapeo a entidad
    private Expensa mapToExpensaEntity(ExpensaCreateDTO dto) throws Exception {
        Consorcio consorcio = validateConsorcio(dto.getIdConsorcio());
        validatePeriodo(dto.getIdConsorcio(), dto.getPeriodo());
        validatePorcentajeIntereses(dto.getPorcentajeIntereses());
        Expensa expensa = new Expensa();

        expensa.setConsorcio(consorcio);
        if(expensaRepository.findByConsorcio_idConsorcio(dto.getIdConsorcio()).isEmpty()){
            expensa.setPeriodo(dto.getPeriodo());
        } else {
            expensa.setPeriodo(dto.getPeriodo().plusMonths(1));
        }

        expensa.setPorcentajeIntereses(dto.getPorcentajeIntereses());
        expensa.setPorcentajeSegundoVencimiento(dto.getPorcentajeSegundoVencimiento());
        return expensa;
    }

    private ExpensaResponseDto mapToExpensaResponse (Expensa expensa) {
        ExpensaResponseDto dto = new ExpensaResponseDto();

        dto.setIdExpensa(expensa.getIdExpensa());
        dto.setIdConsorcio(expensa.getConsorcio().getIdConsorcio());
        dto.setPeriodo(expensa.getPeriodo());
        dto.setPorcentajeIntereses(expensa.getPorcentajeIntereses());
        dto.setPorcentajeSegundoVencimiento(expensa.getPorcentajeSegundoVencimiento());

        List<Egreso> egresos = expensa.getEgresos();
        for(Egreso egreso : egresos){
            dto.getEgresos().add(egresoService.mapToEgresoResponse(egreso));
        }

        List<GastoParticular> gps = expensa.getGastosParticulares();
        for(GastoParticular gp : gps){
            dto.getGp().add(gastoParticularService.mapToGastoParticularResponse(gp));
        }

        List<Ingreso> ingresos = expensa.getIngresos();
        for(Ingreso ingreso : ingresos){
            dto.getIngresos().add(ingresoService.mapToIngresoResponseDTO(ingreso));
        }

        List<PagoUF> pagoUfs = expensa.getPagoUFS();
        for(PagoUF pagoUf : pagoUfs){
            dto.getPagoUf().add(pagoUFService.mapToPagoUFDTO(pagoUf));
        }
        return dto;
    }
}
