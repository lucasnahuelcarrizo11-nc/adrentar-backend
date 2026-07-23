package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Proveedor;
import com.example.adrentar.entity.Resena;
import com.example.adrentar.repository.ProveedorRepository;
import com.example.adrentar.repository.ResenaRepository;
import com.example.adrentar.service.ResenaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaServiceImpl implements ResenaService {

    private final ResenaRepository resenaRepository;
    private final ProveedorRepository proveedorRepository;

    public ResenaServiceImpl(ResenaRepository resenaRepository, ProveedorRepository proveedorRepository) {
        this.resenaRepository = resenaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public Resena crearResena(Resena resena) throws Exception {
        if (resena.getPuntuacion() == null || resena.getPuntuacion() < 1 || resena.getPuntuacion() > 5) {
            throw new Exception("La puntuación debe estar entre 1 y 5");
        }
        if (resena.getProveedor() == null || resena.getProveedor().getIdProveedor() == null) {
            throw new Exception("Falta el proveedor");
        }

        Proveedor proveedor = proveedorRepository.findById(resena.getProveedor().getIdProveedor())
                .orElseThrow(() -> new Exception("Proveedor no encontrado"));

        resena.setProveedor(proveedor);
        return resenaRepository.save(resena);
    }

    @Override
    public List<Resena> listarPorProveedor(Long idProveedor) {
        return resenaRepository.findByProveedor_IdUsuarioOrderByFechaDesc(idProveedor);
    }

    @Override
    public void eliminarResena(Long idResena) throws Exception {
        Resena resena = resenaRepository.findById(idResena)
                .orElseThrow(() -> new Exception("Reseña no encontrada"));
        resenaRepository.delete(resena);
    }

    @Override
    public Double promedioPorProveedor(Long idProveedor) {
        Double promedio = resenaRepository.calcularPromedioPorProveedor(idProveedor);
        return promedio != null ? promedio : 0.0;
    }

    @Override
    public Long cantidadPorProveedor(Long idProveedor) {
        return resenaRepository.contarPorProveedor(idProveedor);
    }
}