package com.banco.backend.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	  
	  private float saldo;
	  
	  private String estado;
	  
	  private int depositosDelDia;
	  
	  private int retirorsDelDia;
	  
	  @JsonBackReference(value = "banco-cuenta")
	  @ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "banco_id")
	  private Banco banco;
	  

	  @JsonBackReference(value = "usuario-cuenta")
	  @ManyToOne(fetch = FetchType.LAZY)
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
}
