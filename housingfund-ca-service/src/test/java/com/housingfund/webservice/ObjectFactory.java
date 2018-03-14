
package com.housingfund.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.housingfund.webservice package. 
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

    private final static QName _GetGJJCompanyInfoResponse_QNAME = new QName("http://caservice.housingfund.handge.com/", "GetGJJCompanyInfoResponse");
    private final static QName _GetGJJCompanyInfo_QNAME = new QName("http://caservice.housingfund.handge.com/", "GetGJJCompanyInfo");
    private final static QName _UnitInfo_QNAME = new QName("http://caservice.housingfund.handge.com/", "unitInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.housingfund.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetGJJCompanyInfoResponse }
     * 
     */
    public GetGJJCompanyInfoResponse createGetGJJCompanyInfoResponse() {
        return new GetGJJCompanyInfoResponse();
    }

    /**
     * Create an instance of {@link GetGJJCompanyInfo }
     * 
     */
    public GetGJJCompanyInfo createGetGJJCompanyInfo() {
        return new GetGJJCompanyInfo();
    }

    /**
     * Create an instance of {@link UnitInfo }
     * 
     */
    public UnitInfo createUnitInfo() {
        return new UnitInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGJJCompanyInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://caservice.housingfund.handge.com/", name = "GetGJJCompanyInfoResponse")
    public JAXBElement<GetGJJCompanyInfoResponse> createGetGJJCompanyInfoResponse(GetGJJCompanyInfoResponse value) {
        return new JAXBElement<GetGJJCompanyInfoResponse>(_GetGJJCompanyInfoResponse_QNAME, GetGJJCompanyInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGJJCompanyInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://caservice.housingfund.handge.com/", name = "GetGJJCompanyInfo")
    public JAXBElement<GetGJJCompanyInfo> createGetGJJCompanyInfo(GetGJJCompanyInfo value) {
        return new JAXBElement<GetGJJCompanyInfo>(_GetGJJCompanyInfo_QNAME, GetGJJCompanyInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnitInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://caservice.housingfund.handge.com/", name = "unitInfo")
    public JAXBElement<UnitInfo> createUnitInfo(UnitInfo value) {
        return new JAXBElement<UnitInfo>(_UnitInfo_QNAME, UnitInfo.class, null, value);
    }

}
