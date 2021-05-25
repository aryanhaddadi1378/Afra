package org.rebecalang.afra.ideplugin;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.osgi.service.datalocation.Location;
import java.net.URL;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.core.runtime.IPlatformRunnable;
import java.io.File;
import java.net.MalformedURLException;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();
		Location instanceLoc = Platform.getInstanceLocation();
		instanceLoc.release();
		URL url = promptForInstanceLoc(display);
		if (url == null) {
			return IPlatformRunnable.EXIT_OK;
		}
		instanceLoc.setURL(url, true);
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
		
	}

	private URL promptForInstanceLoc(Display display) {
		Shell shell = new Shell(display);
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setText("Select Workspace Directory");
		dialog.setMessage("Select the workspace directory to use.");
		String dir = dialog.open();
		shell.dispose();
		try {
			return dir == null ? null : new File(dir).toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}

