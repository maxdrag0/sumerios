package com.mad.sumerios.enums;

import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import lombok.Getter;

@Getter
public enum CategoriaEgreso {
    A {
        @Override
        public void aplicar(EstadoCuentaUf estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalA(total);
        }
        @Override
        public void aplicarAuxiliar(EstadoCuentaUfDTO estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalA(total);
        }
    },
    B {
        @Override
        public void aplicar(EstadoCuentaUf estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalB(total);
        }
        @Override
        public void aplicarAuxiliar(EstadoCuentaUfDTO estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalB(total);
        }
    },
    C {
        @Override
        public void aplicar(EstadoCuentaUf estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalC(total);
        }
        @Override
        public void aplicarAuxiliar(EstadoCuentaUfDTO estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalC(total);
        }
    },
    D {
        @Override
        public void aplicar(EstadoCuentaUf estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalD(total);
        }
        @Override
        public void aplicarAuxiliar(EstadoCuentaUfDTO estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalD(total);
        }
    },
    E {
        @Override
        public void aplicar(EstadoCuentaUf estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalE(total);
        }
        @Override
        public void aplicarAuxiliar(EstadoCuentaUfDTO estadoCuentaUf, double total) {
            estadoCuentaUf.setTotalE(total);
        }
    };

    // Método abstracto que cada categoría implementará
    public abstract void aplicar(EstadoCuentaUf estadoCuentaUf, double total);

    public abstract void aplicarAuxiliar(EstadoCuentaUfDTO estadoCuentaUf, double total);
}
