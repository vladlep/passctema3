package passc;

import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_MultiReceptacle;
import OpenCOM.OpenCOMComponent;


public abstract class BinaryOp extends OpenCOMComponent implements IConnections, ILifeCycle, IMetaInterface, INb, IUnknown {

    protected int Nb;
    protected boolean isEval;
    public OCM_MultiReceptacle<INb> m_PSR_INb;

    public BinaryOp(IUnknown mpIOCM) {
        super(mpIOCM);
        // Initiate the receptacles
        m_PSR_INb = new OCM_MultiReceptacle<INb>(INb.class);
        Nb = 0;
        isEval = false;
    }

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

    public abstract int eval();

    public int getNb() {
        if (isEval) {
            return Nb;
        } else {
            return eval();
        }
    }

    public boolean isEvalNb() {
        return isEval;
    }

    public void reset() {
        m_PSR_INb.interfaceList.get(0).reset();
        m_PSR_INb.interfaceList.get(1).reset();
        isEval = false;
    }

    // ILifeCycle Interface
    public boolean shutdown() {
        return true;
    }

    public boolean startup(Object pIOCM) {
        return true;
    }
}
