package passc;

import OpenCOM.IOpenCOM;
import OpenCOM.IUnknown;

public class PostMethods 
{
	private IOpenCOM pIOCM;
	private IUnknown pIUnk;
	private int connNr;
	private static char c = 'A';
	
	public PostMethods(IOpenCOM pIOCM, IUnknown pIUnk) {
	
	this.pIOCM = pIOCM;
	this.pIUnk = pIUnk;
	this.connNr = 0;
}


	public Object PostAdd(String method, Object result, Object[] args,Exception e)
	{
		return null;
	}

	public Object ConstantChange(String method, Object result, Object[] args,Exception e)
	{
		return e;
		
	}
	
}
