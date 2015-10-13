package ar.com.fwcommon.visores;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

public class VisorJAI extends Visor {

    private PlanarImage planarImage;
//    private DisplayJAI displayJAI;
    private float factorOptimo = 1.0f;
    private float factorAnchoMaximo = 1.0f;
    private float anchoOriginal = 0f;
    private float altoOriginal = 0f;
    private ParameterBlock pb;

    public VisorJAI(String nombreArchivo) {
        super(nombreArchivo);
        File file = new File(nombreArchivo);
        if(file.exists()) {
            setPlanarImage(JAI.create("fileload", nombreArchivo));
            getParametros().addSource(getPlanarImage());
//            getImagen().setName(nombreArchivo);
            setScale(1f);
            anchoOriginal = getPlanarImage().getWidth();
            altoOriginal = getPlanarImage().getHeight();
        }
    }

    /**
     * @return Returns the imagen.
     */
//    public JPanel getImagen() {
//        if(getPlanarImage() != null) {
//            displayJAI = new DisplayJAI(getPlanarImage());
//        }
//        return displayJAI;
//    }

    private PlanarImage getPlanarImage() {
        return planarImage;
    }

    private void setPlanarImage(PlanarImage planarImage) {
        this.planarImage = planarImage;
    }

    /**
     * @return Returns the factorOptimo.
     */
    public float getFactorOptimo() {
        return factorOptimo;
    }

    /**
     * @param factorOptimo The factorOptimo to set.
     */
    public void setFactorOptimo(int ancho, int alto) {
        float factorAncho = 0;
        float factorAlto = 0;
        factorAncho = (float)ancho / anchoOriginal;
        factorAlto = (float)alto / altoOriginal;
        factorOptimo = (factorAncho < factorAlto) ? factorAncho : factorAlto;
    }

    /**
     * @return Returns the factorAnchoMaximo.
     */
    public float getFactorAnchoMaximo() {
        return factorAnchoMaximo;
    }

    /**
     * @param factorOptimo The factorAnchoMaximo to set.
     */
    public void setFactorAnchoMaximo(int ancho) {
        factorAnchoMaximo = (float)ancho / anchoOriginal;
    }

    /**
     * @return Returns the altoOriginal.
     */
    public float getAltoOriginal() {
        return altoOriginal;
    }

    /**
     * @return Returns the anchoOriginal.
     */
    public float getAnchoOriginal() {
        return anchoOriginal;
    }

    public int getWidth() {
        return getPlanarImage().getWidth();
    }

    public int getHeight() {
        return getPlanarImage().getHeight();
    }

    public ParameterBlock getParametros() {
        if(pb == null) {
            pb = new ParameterBlock();
        }
        return pb;
    }

    public void setScale(float factor) {
        getParametros().removeParameters();
        getParametros().add(factor);
        getParametros().add(factor);
        getParametros().add(0.0F);
        getParametros().add(0.0F);
        //getParametros().add(new InterpolationNearest());
        //getParametros().add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
        setPlanarImage(JAI.create("scale", getParametros(), null));
    }

    public void setInset(int h, int v) {
    }

    public void close() {
    }

    public BufferedImage getBufferedImage() {
    	BufferedImage bufferedImage = getPlanarImage().getAsBufferedImage();
        return bufferedImage;
    }

}