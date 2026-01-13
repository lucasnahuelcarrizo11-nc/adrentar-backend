package com.example.adrentar.service;

import com.example.adrentar.entity.Propietario;
import com.example.adrentar.entity.Proveedor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface ProveedorService {

    Proveedor crearProveedor(Proveedor proveedor);

    List<Proveedor> mostrarProveedores();

    Optional<Proveedor> buscarPorNombre(String nombre);

    Optional<Proveedor> buscarPorId(Long idProveedor);

    Proveedor actualizarProveedor(Long idProveedor , Proveedor proveedor) throws Exception;

    void eliminarProveedor(Long idProveedor ) throws Exception;
}
