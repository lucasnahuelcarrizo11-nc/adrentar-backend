package com.example.adrentar.service.impl;

import com.example.adrentar.dto.ProveedorConPromedioDTO;
import com.example.adrentar.entity.Proveedor;
import com.example.adrentar.repository.ProveedorRepository;
import com.example.adrentar.repository.ResenaRepository;
import com.example.adrentar.service.ProveedorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ResenaRepository resenaRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository, ResenaRepository resenaRepository) {
        this.proveedorRepository = proveedorRepository;
        this.resenaRepository = resenaRepository;
    }

    @Override
    public Proveedor crearProveedor(Proveedor proveedor) { return this.proveedorRepository.save(proveedor); }

    @Override
    public List<Proveedor> mostrarProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public List<ProveedorConPromedioDTO> listarProveedoresConPromedio() {
        return proveedorRepository.findAll().stream()
                .map(p -> {
                    Double promedio = resenaRepository.calcularPromedioPorProveedor(p.getIdProveedor());
                    Long cantidad = resenaRepository.contarPorProveedor(p.getIdProveedor());
                    return new ProveedorConPromedioDTO(p, promedio != null ? promedio : 0.0, cantidad);
                })
                .sorted((a, b) -> Double.compare(b.getPromedioPuntuacion(), a.getPromedioPuntuacion()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreCompleto(nombre);
    }

    @Override
    public Optional<Proveedor> buscarPorId(Long idProveedor) {
        return proveedorRepository.findById(idProveedor);
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
        Proveedor proveedorActual = proveedorRepository.findById(idProveedor).orElseThrow(() -> new Exception("Id No encontrado"));
        proveedorRepository.delete(proveedorActual);
    }
}
