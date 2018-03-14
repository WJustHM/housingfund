
package com.handge.housingfund.others.webservice.pdfca;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.handge.housingfund.others.webservice.pdfca package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PdfSignatureToIndexResponse_QNAME = new QName("http://ws.sign.szca.com.cn/", "pdfSignatureToIndexResponse");
    private final static QName _PdfSignatureResponse_QNAME = new QName("http://ws.sign.szca.com.cn/", "pdfSignatureResponse");
    private final static QName _PdfSignature_QNAME = new QName("http://ws.sign.szca.com.cn/", "pdfSignature");
    private final static QName _PdfSignatureToIndex_QNAME = new QName("http://ws.sign.szca.com.cn/", "pdfSignatureToIndex");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.handge.housingfund.others.webservice.pdfca
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PdfSignatureResponse }
     * 
     */
    public PdfSignatureResponse createPdfSignatureResponse() {
        return new PdfSignatureResponse();
    }

    /**
     * Create an instance of {@link PdfSignature }
     * 
     */
    public PdfSignature createPdfSignature() {
        return new PdfSignature();
    }

    /**
     * Create an instance of {@link PdfSignatureToIndex }
     * 
     */
    public PdfSignatureToIndex createPdfSignatureToIndex() {
        return new PdfSignatureToIndex();
    }

    /**
     * Create an instance of {@link PdfSignatureToIndexResponse }
     * 
     */
    public PdfSignatureToIndexResponse createPdfSignatureToIndexResponse() {
        return new PdfSignatureToIndexResponse();
    }

    /**
     * Create an instance of {@link StampPattern }
     * 
     */
    public StampPattern createStampPattern() {
        return new StampPattern();
    }

    /**
     * Create an instance of {@link Stamp }
     * 
     */
    public Stamp createStamp() {
        return new Stamp();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfSignatureToIndexResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sign.szca.com.cn/", name = "pdfSignatureToIndexResponse")
    public JAXBElement<PdfSignatureToIndexResponse> createPdfSignatureToIndexResponse(PdfSignatureToIndexResponse value) {
        return new JAXBElement<PdfSignatureToIndexResponse>(_PdfSignatureToIndexResponse_QNAME, PdfSignatureToIndexResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfSignatureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sign.szca.com.cn/", name = "pdfSignatureResponse")
    public JAXBElement<PdfSignatureResponse> createPdfSignatureResponse(PdfSignatureResponse value) {
        return new JAXBElement<PdfSignatureResponse>(_PdfSignatureResponse_QNAME, PdfSignatureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfSignature }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sign.szca.com.cn/", name = "pdfSignature")
    public JAXBElement<PdfSignature> createPdfSignature(PdfSignature value) {
        return new JAXBElement<PdfSignature>(_PdfSignature_QNAME, PdfSignature.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfSignatureToIndex }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sign.szca.com.cn/", name = "pdfSignatureToIndex")
    public JAXBElement<PdfSignatureToIndex> createPdfSignatureToIndex(PdfSignatureToIndex value) {
        return new JAXBElement<PdfSignatureToIndex>(_PdfSignatureToIndex_QNAME, PdfSignatureToIndex.class, null, value);
    }

}
