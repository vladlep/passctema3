package passc;

import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class Root extends OpenCOMComponent implements IConnections, ILifeCycle, IMetaInterface, INb, IUnknown
{

	protected int Nb;
    protected boolean isEval;
    public OCM_SingleReceptacle<INb> m_PSR_INb;
	public Root(IUnknown mpIOCM) {
		super(mpIOCM);
		m_PSR_INb = new OCM_SingleReceptacle<INb>(INb.class);
		isEval = false;
		Nb = 0;
	}

	@Override
	public boolean connect(IUnknown pSinkIntf, String riid, long provConnID) {
		 if (riid.toString().equalsIgnoreCase("passc.INb")) 
		 {
			 return m_PSR_INb.connectToRecp(pSinkIntf, riid, provConnID); 
		 }
		return false;
	}

	@Override
	public boolean disconnect(String riid, long connID) {
        if (riid.toString().equalsIgnoreCase("passc.INb")) {
            return m_PSR_INb.disconnectFromRecp(connID);
        }
        return false;
	}

	@Override
	public boolean shutdown() {

		return true;
	}

	@Override
	public boolean startup(Object arg0) {
		return true;
	}

	@Override
	public int eval() {
		Nb = m_PSR_INb.m_pIntf.getNb();
		isEval = true;
		return Nb; // nu ne intereseaza cat e rezultatul primit ca nu reconfiguram radacina
	}

	@Override
	public int getNb() {
        if (isEval) {
            return Nb;
        } else {
            return eval();
        }

	}

	@Override
	public boolean isEvalNb() 
	{
	
		return isEval;
	}

	@Override
	public void reset() {
		m_PSR_INb.m_pIntf.reset();
		isEval = false;
		
	}

}
