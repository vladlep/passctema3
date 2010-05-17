package passc;

import OpenCOM.*;

public class DivOp extends BinaryOp implements INb, IConnections, ILifeCycle, IUnknown, IMetaInterface {

    /** Creates a new instance of DivOp */
    public DivOp(IUnknown binder) {
        super(binder);


    }


//Interface INb implementation
    public int eval() {
        Nb = m_PSR_INb.interfaceList.get(0).getNb() / m_PSR_INb.interfaceList.get(1).getNb();
        isEval = true;
        return Nb;
    }
}


