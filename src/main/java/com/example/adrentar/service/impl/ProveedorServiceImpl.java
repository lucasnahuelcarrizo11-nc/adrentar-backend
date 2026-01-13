package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Proveedor;
import com.example.adrentar.repository.ProveedorRepository;
import com.example.adrentar.service.ProveedorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private  ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public Proveedor crearProveedor(Proveedor proveedor) { return this.proveedorRepository.save(proveedor); }

    @Override
    public List<Proveedor> mostrarProveedores() {
      return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedor> buscarPorNombre(String nombre) {
        Optional <Proveedor> provedor1 = proveedorRepository.findByNombreCompleto(nombre);
        return provedor1;
    }

    @Override
    public Optional<Proveedor> buscarPorId(Long idProveedor) {
        Optional <Proveedor> proveedor1 = proveedorRepository.findById(idProveedor);
        return proveedor1;
    }

    @Override
    public Proveedor actualizarProveedor(Long idProveedor, Proveedor proveedor) throws Exception {
        Proveedor proveedorExistente = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado"));

        proveedorExistente.setNombreCompleto(proveedor.getNombreCompleto());
        proveedorExistente.setTelefono(proveedor.getTelefono());
        proveedorExistente.setEmail(proveedor.getEmail());
        proveedorExistente.setEspecialidad(proveedor.getEspecialidad());
        proveedorExistente.setDescripcion(proveedor.getDescripcion());
        proveedorExistente.setZona(proveedor.getZona());
        proveedorExistente.setActivo(proveedor.isActivo());

        return proveedorRepository.save(proveedorExistente);
    }

    @Override
    public void eliminarProveedor(Long idProveedor) throws Exception {
        Proveedor proveedorActual= proveedorRepository.findById(idProveedor).orElseThrow(()-> new Exception("Id No encontrado"));
        proveedorRepository.delete(proveedorActual);

    }
}
