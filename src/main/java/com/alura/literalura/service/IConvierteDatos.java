package com.alura.literalura.service;

public interface IConvierteDatos {
    // El <T> significa que puede devolver CUALQUIER tipo de objeto
    <T> T obtenerDatos(String json, Class<T> clase);
}
