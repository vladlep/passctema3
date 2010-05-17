package passc;

import OpenCOM.*;
import java.io.*;


public class NbSource extends OpenCOMComponent implements INb, ILifeCycle, IUnknown, IMetaInterface {

    private int Nb;
    private boolean isEval;
    private String name;

    /** Creates a new instance of NbSource */
    public NbSource(IUnknown binder) {
        super(binder);
        Nb = 0;
        isEval = false;

    }

//Interface INb implementation
    public int eval() {
        String s;
        IOpenCOM pIOCM = (IOpenCOM) m_PSR_IOpenCOM.m_pIntf.QueryInterface("OpenCOM.IOpenCOM");
        name = new String(pIOCM.getComponentName(this));

        System.out.println("Get number value for: " + name);
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader stdin = new BufferedReader(inputStreamReader);
        try {
            s = stdin.readLine();
            Nb = new Integer(s);
            isEval = true;
            return Nb;
        } catch (Exception e) {
        }
        isEval = false;
        return Nb;
    }

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
