package passc;

import OpenCOM.*;


public class AddOp extends BinaryOp implements IConnections, ILifeCycle, IMetaInterface, INb, IUnknown {

    public AddOp(IUnknown mpIOCM) {
        super(mpIOCM);

    }

    public int eval() {
        Nb = m_PSR_INb.interfaceList.get(0).getNb() + m_PSR_INb.interfaceList.get(1).getNb();
        isEval = true;
        return Nb;
    }
}
