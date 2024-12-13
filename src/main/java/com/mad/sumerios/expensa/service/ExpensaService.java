package com.mad.sumerios.expensa.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.estadocuentauf.repository.IEstadoCuentaUfRepository;
import com.mad.sumerios.estadocuentauf.service.EstadoCuentaUfService;
import com.mad.sumerios.expensa.dto.ExpensaCreateDTO;
import com.mad.sumerios.expensa.model.Expensa;
import com.mad.sumerios.expensa.repository.IExpensaRepository;
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class ExpensaService {

    private final IExpensaRepository expensaRepository;
    private final IConsorcioRepository consorcioRepository;
    private final IUnidadFuncionalRepository unidadFuncionalRepository;
    private final IEstadoCuentaUfRepository estadoCuentaUfRepository;
    private final EstadoCuentaUfService estadoCuentaUfService;


    @Autowired
    public ExpensaService (IExpensaRepository expensaRepository,
                           IConsorcioRepository consorcioRepository,
                           IUnidadFuncionalRepository unidadFuncionalRepository,
                           EstadoCuentaUfService estadoCuentaUfService,
                           IEstadoCuentaUfRepository estadoCuentaUfRepository){
        this.expensaRepository = expensaRepository;
        this.consorcioRepository = consorcioRepository;
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.estadoCuentaUfService = estadoCuentaUfService;
        this.estadoCuentaUfRepository = estadoCuentaUfRepository;
    }

//    @Transactional
//    public void liquidarExpensa(Long idExpensa,ExpensaCreateDTO dto) throws Exception{
//        Expensa expensa = expensaRepository.findById(idExpensa)
//                .orElseThrow(()-> new Exception("Expensa no encontrado"));
//        List<UnidadFuncional> ufs = unidadFuncionalRepository.findByConsorcio_IdConsorcio(expensa.getConsorcio().getIdConsorcio());
//        List<EstadoCuentaUf> estadosDeCuentaUf = estadoCuentaUfRepository.findByUnidadFuncionalIn(ufs);

    // CREA COPIA DE ECUF ACTUAL
//        createCopiasEstadoDeCuentaUf(estadosDeCuentaUf);
    // SUMA GASTOS A, B, C, D Y E
//        double totalA = totalGastos(expensa.getEgresos(),CategoriaEgreso.A);
//        double totalA = totalGastos(expensa.getEgresos(),CategoriaEgreso.B);
//        double totalA = totalGastos(expensa.getEgresos(),CategoriaEgreso.C);
//        double totalA = totalGastos(expensa.getEgresos(),CategoriaEgreso.D);
//        double totalA = totalGastos(expensa.getEgresos(),CategoriaEgreso.E);
    // ECUF -> APLICA A ESTADO DE CUENTA UF POR PORCENTAJE SUMATORIA DE CADA GASTO
    // ECUF -> APLICA VALOR POR GASTOS PARTICULARES O APLICA 0
    // ECUF -> TOMA EL SALDO INTERESES Y LO TRANSFORMA EN DEUDA Y CALCULA EL INTERES
    // ECUF -> SUMA DEUDA, INTERES, GASTOS PARTICULARES Y TOTALES Y DA EL TOTAL FINAL
    // ECUF -> APLICA EL SALDO EXPENSA CON LA SUMA DE LOS TOTALES Y SALDO ITNERESES CON LOS INTERESES
    // ELIMINA ENTIDAD INTERMEDIA PARA SABER LA ÚLTIMA EXPENSA CREADA EN EL CONSORCIO
    // CREA NUEVA EXPENSA  VACIA
//        createExpensa(dto);
    // CREA ENTIDAD INTERMEDIA PARA SABER LA ULTIMA EXPENSA LIQUIDADA DEL CONSORCIO
//    }



    public void createExpensa(ExpensaCreateDTO dto) throws Exception {
        Expensa exp = mapToExpensaEntity(dto);
        expensaRepository.save(exp);
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
    // Mapeo a entidad
    private Expensa mapToExpensaEntity(ExpensaCreateDTO dto) throws Exception {
        Consorcio consorcio = validateConsorcio(dto.getIdConsorcio());
        validatePeriodo(dto.getIdConsorcio(), dto.getPeriodo());
        validatePorcentajeIntereses(dto.getPorcentajeIntereses());
        Expensa expensa = new Expensa();

        expensa.setConsorcio(consorcio);
        expensa.setPeriodo(dto.getPeriodo());
        expensa.setPorcentajeIntereses(dto.getPorcentajeIntereses());
        expensa.setPorcentajeSegundoVencimiento(dto.getPorcentajeSegundoVencimiento());
        return expensa;
    }


}
