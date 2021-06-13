package br.com.carlos.todolist.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.carlos.todolist.model.comum.Json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;


/**
 * @author carlos.oliveira
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "TDL_USUARIO")
public class Usuario extends Json implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
    @SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "SEQ_TDL_USUARIO", allocationSize = 10, initialValue = 10)
    private Long id;

    @NotNull
    @Column(name = "LOGIN")
    private String login;

    private String password;

    @Column(name = "SUPER_USER")
    private Boolean superUser = false;

    public Usuario(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Usuario(String login, String password, Boolean superUser) {
        this(login, password);
        this.superUser = superUser;
    }

    @NotNull
    @JsonIgnore
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSuperUser() {
        return superUser;
    }

    @JsonProperty
    public void setSuperUser(Boolean superUser) {
        this.superUser = superUser;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.login;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @SneakyThrows
    public static Usuario fromJson(String usuarioStr) {
        return new ObjectMapper().readValue(usuarioStr, Usuario.class);
    }

    public Usuario clone() {
        return SerializationUtils.clone(this);
    }
}
