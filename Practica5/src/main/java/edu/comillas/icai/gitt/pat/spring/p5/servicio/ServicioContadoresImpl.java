package edu.comillas.icai.gitt.pat.spring.p5.servicio;

import edu.comillas.icai.gitt.pat.spring.p5.entidad.Contador;
import edu.comillas.icai.gitt.pat.spring.p5.entidad.Operacion;
import edu.comillas.icai.gitt.pat.spring.p5.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.p5.repositorio.RepoContador;
import edu.comillas.icai.gitt.pat.spring.p5.repositorio.RepoOperacion;
import edu.comillas.icai.gitt.pat.spring.p5.repositorio.RepoUsuario;
import jakarta.transaction.Transactional;

import org.hibernate.type.TrueFalseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class ServicioContadoresImpl implements ServicioContadores {
    @Autowired
    RepoContador repoContador;
    @Autowired
    RepoOperacion repoOperacion;
    @Autowired
    RepoUsuario repoUsuario;

    @Override
    @Transactional
    public Usuario autentica(String credenciales) {
        Usuario usuario = repoUsuario.findByCredenciales(credenciales);
        if (usuario == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        else{
            return usuario;
        }
    }
    public Operacion creaOperation(Usuario usuario, Contador contador, String tipo){
        
        
        Operacion operacion = new Operacion();
        operacion.setContadorId(contador.getId());
        operacion.setFecha(Timestamp.from(Instant.now()));
        operacion.setUsuarioId(usuario.getId());
        operacion.setTipo(tipo);
        
        repoOperacion.save(operacion);
        
        return operacion;
    }
    
    @Override
    public Contador crea(Contador contadorNuevo, Usuario usuario) {
        Contador contador = repoContador.save(contadorNuevo);
        creaOperation(usuario,contadorNuevo,"Crear");
        return contador;

    }
    @Override
    public Contador lee(String nombre, Usuario usuario) throws ResponseStatusException{ 
        Contador contador = repoContador.findByNombre(nombre);
        if (contador != null){
            creaOperation(usuario, contador, "Leer");
            return contador;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }
    @Override
    public Contador incrementa(Contador contador, Long incremento, Usuario usuario) {
        if (repoContador.existsById(contador.getId())){
            
            contador.setValor(contador.getValor() + incremento);
            repoContador.save(contador); 


            creaOperation(usuario, contador, "Incrementar");
            
            return contador;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }
    @Override
    public void borra(Contador contador, Usuario usuario) {    
        repoContador.deleteById(contador.getId());
        creaOperation(usuario, contador, "Borrar");
    }


    @Override
    public List<Operacion> operaciones(Contador contador) { 
        List<Operacion> listaOperaciones = new ArrayList<>();
        listaOperaciones = repoOperacion.findAllByContadorId(contador.getId());      
        
        return listaOperaciones;   
    }   
}
