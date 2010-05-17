package passc;

import OpenCOM.*;

public class Monitor extends OpenCOMComponent implements IMonitor, IConnections, ILifeCycle, IUnknown, IMetaInterface {

    
    /**
     * Requires Interfaces of type INb - monitor will be connected to the outpouts of all arithmetic components
     */
    public OCM_MultiReceptacle<INb> m_PSR_INb;

    /** Creates a new instance of Monitor */
    public Monitor(IUnknown binder) {
        super(binder);

        // Initiate the receptacles
        m_PSR_INb = new OCM_MultiReceptacle<INb>(INb.class);

       
    }


// IMonitor interface
    public void monitor() {

// TO DO: implement monitoring and reconfiguration rules
        
    }

   
// IConnections Interface
    public boolean connect(IUnknown pSinkIntf, String riid, long provConnID) {
        if (riid.toString().equalsIgnoreCase("passc.INb")) {
            return m_PSR_INb.connectToRecp(pSinkIntf, riid, provConnID);
        }
        return false;
    }

    public boolean disconnect(String riid, long connID) {

        if (riid.toString().equalsIgnoreCase("passc.INb")) {
            return m_PSR_INb.disconnectFromRecp(connID);
        }
        return false;
    }

    // ILifeCycle Interface
    public boolean shutdown() {
        return true;
    }

    public boolean startup(Object pIOCM) {
        return true;
    }
}
