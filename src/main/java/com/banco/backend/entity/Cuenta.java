package com.banco.backend.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cuenta")
public class Cuenta extends AudtiModel {

	private static final long serialVersionUID = 1L;

	  @Id
	  @GeneratedValue(generator = "uuid2")
	  @GenericGenerator(name = "uuid2", strategy = "uuid2")
	  @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
	  @Type(type = "uuid-char")
	  private UUID id;
	  

	  private long cci;
	  
	  private BigDecimal saldo;
	  
	  private BigDecimal limiteDelDia;
	  
	  private String estado;
	  
	  private int depositosDelDia;
	  
	  private int retirosDelDia;
	  
	  @JsonBackReference(value = "banco-cuenta")
	  @ManyToOne
	  @JoinColumn(name = "banco_id")
	  private Banco banco;
	  
	  @JsonBackReference(value = "usuario-cuenta")
	  @ManyToOne
	  @JoinColumn(name = "usuario_id")
	  private Usuario usuario;
	  
	  @OneToOne
	  @JoinColumn(name = "tipoCuenta_id")
	  private TipoCuenta tipoCuenta;
	  
	  @JsonManagedReference(value = "movimientos-cuenta")
	  @OneToMany(mappedBy = "cuenta",cascade = CascadeType.ALL,orphanRemoval = true)
	  private Set<Movimiento> movimientos = new HashSet<>();
	  
	  @JsonManagedReference(value = "cuenta-prestamo")
	  @OneToMany(mappedBy = "cuenta",cascade = CascadeType.ALL,orphanRemoval = true)
	  private Set<Prestamo> prestamo = new HashSet<>();
	  
	  @JsonManagedReference(value = "cuenta-solicitud")
	  @OneToMany(mappedBy = "cuenta")
	  private List<SolicitudHabilitarCuenta> solicitudHabilitarCuenta = new ArrayList<>();
	  
	  
	  @PrePersist
	  public void generarCci() {
		  Random random = new Random();
		  cci = random.nextLong() % 10000000000L;
		  if(cci < 0) {
			  cci = -cci;
		  }
	  }
}
