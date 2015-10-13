package ar.com.fwcommon.visores;

import java.awt.image.BufferedImage;

import ar.com.fwcommon.componentes.error.FWException;

public abstract class Visor {

    private String nombreArchivo;

    public Visor(String nombreArchivo) {
        super();
        setNombreArchivo(nombreArchivo);
    }

//    public abstract JPanel getImagen();

    public abstract float getFactorOptimo();

    public abstract void setFactorOptimo(int ancho, int alto);

    public abstract float getFactorAnchoMaximo();

    public abstract void setFactorAnchoMaximo(int ancho);

    public abstract float getAltoOriginal();

    public abstract float getAnchoOriginal();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void setScale(float factor);

    public abstract void setInset(int h, int v);

    public abstract void close();
    
    public abstract BufferedImage getBufferedImage() throws FWException;

    /**
     * @return Returns the nombreArchivo.
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param nombreArchivo The nombreArchivo to set.
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

}