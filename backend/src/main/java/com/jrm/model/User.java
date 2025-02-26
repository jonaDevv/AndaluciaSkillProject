package com.jrm.model;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;


import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data  @AllArgsConstructor
@NoArgsConstructor
@Validated
@Entity
public class User implements UserDetails {


   
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	
	@NotNull(message = "El Dnino puede ser nulo")
	@Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "El DNI debe tener 8 dígitos seguidos de una letra")
    @Column(unique = true)
    private String dni;

	@NotNull(message = "El nombre no puede ser nulo")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "La descripcion solo debe contener letras")
    private String nombre;

	@NotNull(message = "El username no puede ser nulo")
	@Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "El username debe ser un correo electronico")
    @Column(unique = true)
    private String username;

	@NotNull(message = "La password no puede ser nulo")
    private String password;

   	@ManyToOne
    @JoinColumn(name = "specialty_id", nullable=true) 
    private Specialty specialty;

    @ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<UserRole> roles;



    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.name())).collect(Collectors.toList());
	}


    /**
	 * No vamos a gestionar la expiración de cuentas. De hacerse, se tendría que dar
	 * cuerpo a este método
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * No vamos a gestionar el bloqueo de cuentas. De hacerse, se tendría que dar
	 * cuerpo a este método
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * No vamos a gestionar la expiración de cuentas. De hacerse, se tendría que dar
	 * cuerpo a este método
	 */

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	
	/**
	 * No vamos a gestionar el bloqueo de cuentas. De hacerse, se tendría que dar
	 * cuerpo a este método
	 */	
	@Override
	public boolean isEnabled() {
		return true;
	}

}
