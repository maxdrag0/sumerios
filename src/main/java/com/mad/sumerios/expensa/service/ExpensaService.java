package com.mad.sumerios.expensa.service;

import com.mad.sumerios.administracion.service.AdministracionService;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.consorcio.service.ConsorcioService;
import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.estadocuentaconsorcio.service.EstadoCuentaConsorcioService;
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
import com.mad.sumerios.pdf.PdfGeneratorExpensa;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import com.mad.sumerios.unidadfuncional.service.UnidadFuncionalService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpensaService {

    private final IExpensaRepository expensaRepository;
    private final IConsorcioRepository consorcioRepository;
    private final ConsorcioService consorcioService;
    private final IUnidadFuncionalRepository unidadFuncionalRepository;
    private final UnidadFuncionalService unidadFuncionalService;
    private final EstadoCuentaConsorcioService estadoCuentaConsorcioService;
    private final IEstadoCuentaUfRepository estadoCuentaUfRepository;
    private final EstadoCuentaUfService estadoCuentaUfService;
    private final IIntermediaExpensaConsorcioRepository intermediaExpensaConsorcioRepository;
    private final IntermediaExpensaConsorcioService intermediaExpensaConsorcioService;
    private final EgresoService egresoService;
    private final GastoParticularService gastoParticularService;
    private final IngresoService ingresoService;
    private final PagoUFService pagoUFService;
    private final AdministracionService administracionService;

    @Autowired
    public ExpensaService (IExpensaRepository expensaRepository,
                           IConsorcioRepository consorcioRepository,
                           ConsorcioService consorcioService,
                           IUnidadFuncionalRepository unidadFuncionalRepository,
                           UnidadFuncionalService unidadFuncionalService,
                           EstadoCuentaConsorcioService estadoCuentaConsorcioService,
                           EstadoCuentaUfService estadoCuentaUfService,
                           IEstadoCuentaUfRepository estadoCuentaUfRepository,
                           IIntermediaExpensaConsorcioRepository intermediaExpensaConsorcioRepository,
                           IntermediaExpensaConsorcioService intermediaExpensaConsorcioService,
                           EgresoService egresoService,
                           GastoParticularService gastoParticularService,
                           IngresoService ingresoService,
                           PagoUFService pagoUFService,
                           AdministracionService administracionService){
        this.expensaRepository = expensaRepository;
        this.consorcioRepository = consorcioRepository;
        this.consorcioService = consorcioService;
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.unidadFuncionalService = unidadFuncionalService;
        this.estadoCuentaConsorcioService = estadoCuentaConsorcioService;
        this.estadoCuentaUfService = estadoCuentaUfService;
        this.estadoCuentaUfRepository = estadoCuentaUfRepository;
        this.intermediaExpensaConsorcioRepository = intermediaExpensaConsorcioRepository;
        this.intermediaExpensaConsorcioService = intermediaExpensaConsorcioService;
        this.egresoService = egresoService;
        this.gastoParticularService = gastoParticularService;
        this.ingresoService = ingresoService;
        this.pagoUFService = pagoUFService;
        this.administracionService = administracionService;
    }

    public byte[] previsualizarExpensa(Long idConsorcio, Long idExpensa, ExpensaCreateDTO dto, Boolean segundoVencimiento) throws Exception{
        validateConsorcio(idConsorcio);
        validateUltimoPeriodo(idConsorcio, dto.getPeriodo());
        ExpensaResponseDto expensa = this.getExpensasById(idExpensa);
        ConsorcioResponseDTO consorcio = consorcioService.getConsorcioById(idConsorcio);
        List<UnidadFuncionalResponseDTO> ufs = unidadFuncionalService.getUnidadesPorConsorcio(idConsorcio);
        List<EstadoCuentaUfDTO> estadosDeCuentaUf = estadoCuentaUfService.getEstadoCuentaUfs(ufs);

        List<EstadoCuentaUfDTO> estadosDeCuentaAuxiliares = estadoCuentaUfService.createEstadosDeCuentaAuxiliares(estadosDeCuentaUf);
        aplicarDeudaEInteresesAuxiliar(estadosDeCuentaAuxiliares, expensa.getPorcentajeIntereses());

        Map<CategoriaEgreso, Double> totalGastosPorCategoria = Arrays.stream(CategoriaEgreso.values())
                .collect(Collectors.toMap(categoria -> categoria, categoria -> totalGastos(expensa.getEgresos(), categoria)));

        totalGastosPorCategoria.forEach((categoria, total) -> {
            if (total > 0) {
                try {
                    aplicarTotalAuxiliar(estadosDeCuentaAuxiliares, total, categoria);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if(!expensa.getGp().isEmpty()){
            aplicarGastosParticularesAuxiliar(expensa.getGp(), estadosDeCuentaAuxiliares);
        }

        aplicarValorTotalAuxiliar(estadosDeCuentaAuxiliares);


        calcularSegundoVencimientoAuxiliar(estadosDeCuentaAuxiliares, dto.getPorcentajeSegundoVencimiento(), segundoVencimiento);

        return PdfGeneratorExpensa.crearPdfExpensa(
                administracionService.getAdministracionById(consorcio.getIdAdm()),
                consorcio,
                expensa,
                ufs,
                estadosDeCuentaAuxiliares,
                segundoVencimiento,
                dto.getNota(),
                dto.getJuicios());
    }


    @Transactional
    public byte[] liquidarExpensaMesVencido(Long idConsorcio, Long idExpensa, ExpensaCreateDTO dto, Boolean repetirEgresos, Boolean segundoVencimiento) throws Exception{
        validateConsorcio(idConsorcio);
        validateUltimoPeriodo(idConsorcio, dto.getPeriodo());
        ExpensaResponseDto expensa = this.getExpensasById(idExpensa);
        ConsorcioResponseDTO consorcio = consorcioService.getConsorcioById(idConsorcio);
        List<UnidadFuncionalResponseDTO> ufs = unidadFuncionalService.getUnidadesPorConsorcio(idConsorcio);
        List<EstadoCuentaUfDTO> estadosDeCuentaUf = estadoCuentaUfService.getEstadoCuentaUfs(ufs);

        // CREA COPIA DE ECUF ACTUAL
        createCopiasEstadoDeCuentaUf(estadosDeCuentaUf);

        // ECUF -> TOMA EL SALDO EXPENSAS Y LO TRANSFORMA EN DEUDA Y CALCULA EL INTERES
        aplicarDeudaEIntereses(estadosDeCuentaUf, expensa.getPorcentajeIntereses());

        // Aplicar gastos por categoría
        Map<CategoriaEgreso, Double> totalGastosPorCategoria = Arrays.stream(CategoriaEgreso.values())
                .collect(Collectors.toMap(categoria -> categoria, categoria -> totalGastos(expensa.getEgresos(), categoria)));

        totalGastosPorCategoria.forEach((categoria, total) -> {
            if (total > 0) {
                try {
                    aplicarTotal(estadosDeCuentaUf, total, categoria);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    // ECUF -> APLICA VALOR POR GASTOS PARTICULARES O APLICA 0
        if(!expensa.getGp().isEmpty()){
            aplicarGastosParticulares(expensa.getGp());
        }

    // ECUF -> SUMA DEUDA, INTERES, GASTOS PARTICULARES Y TOTALES Y DA EL TOTAL FINAL, APLICA EL SALDO EXPENSA CON LA SUMA DE LOS TOTALES
        aplicarValorTotal(estadosDeCuentaUf);

        // CALCULA SEGUNDO VENCIMIENTO
        calcularSegundoVencimiento(estadosDeCuentaUf, dto.getPorcentajeSegundoVencimiento(), segundoVencimiento);
        ajustarInteresesSegundoVencimientoConsorcio(consorcio.getIdConsorcio(), dto.getPorcentajeIntereses(), segundoVencimiento ,dto.getPorcentajeSegundoVencimiento());

    // CREA NUEVA EXPENSA  VACIA E INTNERMEDIA
        createExpensa(dto);

        if(repetirEgresos){
            ExpensaResponseDto expNueva = this.getExpensasByConsorcioAndPeriodo(idConsorcio,expensa.getPeriodo().plusMonths(1));
            egresoService.createEgresosNuevaExpensa(expNueva.getIdExpensa(),expensa.getEgresos());
        }

        List<UnidadFuncionalResponseDTO> ufsUpdate = unidadFuncionalService.getUnidadesPorConsorcio(idConsorcio);
        List<EstadoCuentaUfDTO> estadosDeCuentaUfUpdate = estadoCuentaUfService.getEstadoCuentaUfs(ufsUpdate);

//      CREO PDF Y LO DEVUELV
        byte[] pdfBytes = PdfGeneratorExpensa.crearPdfExpensa(administracionService.getAdministracionById(consorcio.getIdAdm()), consorcio, expensa, ufsUpdate, estadosDeCuentaUfUpdate,segundoVencimiento, dto.getNota(), dto.getJuicios());

        // ACTUALIZO EL TOTAL AL INICIO DEL PERIODO
        estadoCuentaConsorcioService.actualizarTotalAlCierre(consorcio.getEstadoCuentaConsorcioDTO().getIdEstadoCuentaConsorcio(),consorcio.getEstadoCuentaConsorcioDTO().getTotal());
//        // Retornar el PDF
        return pdfBytes;
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
        ExpensaResponseDto expensaAnterior = this.getExpensasByConsorcioAndPeriodo(expensaUltima.getIdConsorcio(), expensaUltima.getPeriodo().minusMonths(1));

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

        intermedia.setIdExpensa(expensaAnterior.getIdExpensa());
        intermedia.setPeriodo(expensaAnterior.getPeriodo());
        intermediaExpensaConsorcioService.updateIntermediaExpensaConsorcio(intermedia);

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
    private double diferenciaRedondeo(double numero) {
        double numeroRedondeado = Math.ceil(numero);
        return numeroRedondeado - numero;
    }
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
    private double totalGastos(List<EgresoResponseDTO> egresos,
                               CategoriaEgreso categoriaEgreso) {
        if (egresos == null || egresos.isEmpty() || categoriaEgreso == null) {
            return 0.0;
        }

        return egresos.stream()
                .filter(egreso -> categoriaEgreso.equals(egreso.getCategoriaEgreso()))
                .mapToDouble(EgresoResponseDTO::getTotalFinal)
                .sum();
    }

    private void aplicarDeudaEIntereses(List<EstadoCuentaUfDTO> estadosDeCuentaUf,
                                        double porcentajeIntereses) throws Exception {
        for (EstadoCuentaUfDTO estadoActual : estadosDeCuentaUf) {
            if(estadoActual.getSaldoFinal() > 0){
                estadoCuentaUfService.aplicarDeudaEIntereses(estadoActual.getIdEstadoCuentaUf(), porcentajeIntereses);
            } else {
                EstadoCuentaUf ecuf = estadoCuentaUfRepository.findById(estadoActual.getIdEstadoCuentaUf())
                        .orElseThrow(()-> new Exception("Estado de cuenta no encontrado"));

                ecuf.setDeuda(ecuf.getSaldoExpensa());
                ecuf.setTotalMesPrevio(ecuf.getTotalExpensa());
                ecuf.setIntereses(0.0);
                ecuf.setSaldoFinal(0.0);
                ecuf.setSaldoExpensa(0.0);
                ecuf.setSaldoIntereses(0.0);

                estadoCuentaUfRepository.save(ecuf);
            }
        }
    }

    private void aplicarDeudaEInteresesAuxiliar(List<EstadoCuentaUfDTO> estadosDeCuentaAuxiliares,
                                                double porcentajeIntereses) {
        for(EstadoCuentaUfDTO ecAux : estadosDeCuentaAuxiliares){
            ecAux.setTotalMesPrevio(ecAux.getTotalExpensa());
            if(ecAux.getSaldoFinal()>0){
                double deuda = ecAux.getSaldoExpensa() - ecAux.getSaldoIntereses();
                double intereses = (deuda * porcentajeIntereses) / 100;

                ecAux.setDeuda(ecAux.getSaldoFinal());
                ecAux.setIntereses(intereses);
                ecAux.setSaldoFinal(0.0);
                ecAux.setSaldoIntereses(intereses);
            } else {
                ecAux.setDeuda(ecAux.getSaldoExpensa());
                ecAux.setIntereses(0.0);
                ecAux.setSaldoFinal(0.0);
                ecAux.setSaldoExpensa(0.0);
                ecAux.setSaldoIntereses(0.0);
            }
        }
    }

    private void aplicarTotal(List<EstadoCuentaUfDTO> estadoCuentaUfs,
                              double total,
                              CategoriaEgreso categoria) throws Exception {
        for (EstadoCuentaUfDTO estadoActual : estadoCuentaUfs) {
            UnidadFuncionalResponseDTO uf = unidadFuncionalService.getUnidadFuncionalById(estadoActual.getIdUf());

            // Mapa que asocia cada categoría con su porcentaje correspondiente
            Map<CategoriaEgreso, Double> porcentajes = Map.of(
                    CategoriaEgreso.A, uf.getPorcentajeUnidad(),
                    CategoriaEgreso.B, uf.getPorcentajeUnidadB(),
                    CategoriaEgreso.C, uf.getPorcentajeUnidadC(),
                    CategoriaEgreso.D, uf.getPorcentajeUnidadD(),
                    CategoriaEgreso.E, uf.getPorcentajeUnidadE()
            );

            // Obtener el porcentaje de la categoría dada
            Double porcentaje = porcentajes.get(categoria);

            if (porcentaje != null) {
                estadoCuentaUfService.aplicarValorCategoria(
                        estadoActual.getIdEstadoCuentaUf(),
                        porcentaje,
                        total,
                        categoria
                );
            }
        }
    }


    private void aplicarTotalAuxiliar(List<EstadoCuentaUfDTO> estadosDeCuentaAuxiliares,
                                      double total,
                                      CategoriaEgreso categoria) throws Exception {
        for(EstadoCuentaUfDTO ecAux: estadosDeCuentaAuxiliares){
            UnidadFuncionalResponseDTO uf = unidadFuncionalService.getUnidadFuncionalById(ecAux.getIdUf());

            Map<CategoriaEgreso, Double> porcentajes = Map.of(
                    CategoriaEgreso.A, uf.getPorcentajeUnidad(),
                    CategoriaEgreso.B, uf.getPorcentajeUnidadB(),
                    CategoriaEgreso.C, uf.getPorcentajeUnidadC(),
                    CategoriaEgreso.D, uf.getPorcentajeUnidadD(),
                    CategoriaEgreso.E, uf.getPorcentajeUnidadE()
            );

            Double porcentaje = porcentajes.get(categoria);
            if (porcentaje != null) {
                double totalAAplicar = (porcentaje > 0) ? (total * porcentaje) / 100 : 0;

                if (totalAAplicar>0){
                    categoria.aplicarAuxiliar(ecAux, totalAAplicar);
                }
            }
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

    private void aplicarGastosParticularesAuxiliar(List<GastoParticularResponseDTO> gastosParticulares,
                                                   List<EstadoCuentaUfDTO> estadosDeCuentaAuxiliares){
        for(GastoParticularResponseDTO gp : gastosParticulares){
            double valor = gp.getTotalFinal();
            if(valor>0){
                estadosDeCuentaAuxiliares.stream()
                        .filter(ec -> ec.getIdUf().equals(gp.getIdUf()))
                        .findFirst()
                        .ifPresent(ecAux -> ecAux.setGastoParticular(ecAux.getGastoParticular() + valor));
            }
        }
    }

    private void aplicarValorTotal(List<EstadoCuentaUfDTO> estadosDeCuentaUf) throws Exception {
        for (EstadoCuentaUfDTO estadoActual : estadosDeCuentaUf) {
            estadoCuentaUfService.aplicarTotalFinal(estadoActual.getIdEstadoCuentaUf());
        }
    }

    private void aplicarValorTotalAuxiliar(List<EstadoCuentaUfDTO> estadosDeCuentaAuxiliares) {
        for(EstadoCuentaUfDTO ecAux : estadosDeCuentaAuxiliares){

            double valorDeExpensa = ecAux.getTotalA() +
                    ecAux.getTotalB() +
                    ecAux.getTotalC() +
                    ecAux.getTotalD() +
                    ecAux.getTotalE() +
                    ecAux.getGastoParticular();

            double valorDeudaEIntereses = ecAux.getDeuda() + ecAux.getIntereses();
            double valorFinal = valorDeExpensa+valorDeudaEIntereses;
            double redondeo = diferenciaRedondeo(valorFinal);

            ecAux.setRedondeo(redondeo);
            ecAux.setSaldoExpensa(valorDeExpensa + ecAux.getSaldoExpensa());
            ecAux.setSaldoFinal(valorFinal+redondeo);
            ecAux.setTotalExpensa(valorFinal+redondeo);
        }
    }

    private void calcularSegundoVencimiento(List<EstadoCuentaUfDTO> estadosDeCuentaUf, Double porcentajeSegundoVencimiento, Boolean segundoVencimiento) throws Exception {
        for (EstadoCuentaUfDTO estadoActual : estadosDeCuentaUf) {
            if(segundoVencimiento){
                estadoCuentaUfService.calcularSegundoVencimiento(estadoActual.getIdEstadoCuentaUf(), porcentajeSegundoVencimiento);
            }
        }
    }

    private void calcularSegundoVencimientoAuxiliar(List<EstadoCuentaUfDTO> estadosDeCuentaAuxiliares, Double porcentajeSegundoVencimiento, Boolean segundoVencimiento) {
        for(EstadoCuentaUfDTO ecAux : estadosDeCuentaAuxiliares){
            if(segundoVencimiento){
                ecAux.setSegundoVencimiento(ecAux.getTotalExpensa() + ((ecAux.getTotalExpensa() * porcentajeSegundoVencimiento) / 100));
            } else if(ecAux.getSegundoVencimiento() != 0.0) {
                ecAux.setSegundoVencimiento(0.0);
            }
        }
    }

    private void ajustarInteresesSegundoVencimientoConsorcio(Long idConsorcio, Double porcentajeIntereses, Boolean segundoVencimiento, Double porcentajeSegundoVencimiento) throws Exception {
        consorcioService.ajustarInteresesSegundoVencimientoConsorcio(idConsorcio, porcentajeIntereses, segundoVencimiento, porcentajeSegundoVencimiento);
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
