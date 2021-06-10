/**
@autor proamazonia [Christian Báez]  20 may. 2021

**/
package ec.gob.ambiente.sis.utils.sesion;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
public class ViewExpiredExceptionHandlerFactory extends ExceptionHandlerFactory{

	/**
	 * 
	 */
	private ExceptionHandlerFactory factory;
	@SuppressWarnings("deprecation")
	public ViewExpiredExceptionHandlerFactory(ExceptionHandlerFactory factory) {
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see javax.faces.context.ExceptionHandlerFactory#getExceptionHandler()
	 */
	@Override
	public ExceptionHandler getExceptionHandler() {
		ExceptionHandler handler = factory.getExceptionHandler();
		handler = (ExceptionHandler) new ViewExpiredExceptionHandler(handler);
		return handler;
	}

}