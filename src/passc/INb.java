package passc;

import OpenCOM.*;


public interface INb extends IUnknown {
    public int eval();
    public int getNb();
    public boolean isEvalNb ();
    public void reset();
}
