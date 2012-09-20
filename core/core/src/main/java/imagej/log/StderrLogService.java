/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2012 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package imagej.log;

import imagej.Priority;
import imagej.plugin.Plugin;
import imagej.service.AbstractService;
import imagej.service.Service;

/**
 * Implementation of {@link LogService} using the standard error stream.
 * 
 * @author Johannes Schindelin
 */
@Plugin(type = Service.class, priority = Priority.LOW_PRIORITY)
public class StderrLogService extends AbstractService implements LogService {

	// -- LogService methods --

	@Override
	public void debug(Object msg) {
		if (isDebug()) {
			log("[DEBUG] ", msg);
		}
	}

	@Override
	public void debug(Throwable t) {
		if (isDebug()) {
			log("[DEBUG] ", t);
		}
	}

	@Override
	public void debug(Object msg, Throwable t) {
		if (isDebug()) {
			debug(msg);
			debug(t);
		}
	}

	@Override
	public void error(Object msg) {
		log("[ERROR] ", msg);
	}

	@Override
	public void error(Throwable t) {
		log("[ERROR] ", t);
	}

	@Override
	public void error(Object msg, Throwable t) {
		error(msg);
		error(t);
	}

	@Override
	public void info(Object msg) {
		log("[INFO] ", msg);
	}

	@Override
	public void info(Throwable t) {
		log("[INFO] ", t);
	}

	@Override
	public void info(Object msg, Throwable t) {
		info(msg);
		info(t);
	}

	@Override
	public void trace(Object msg) {
		log("[TRACE] ", msg);
	}

	@Override
	public void trace(Throwable t) {
		log("[TRACE] ", t);
	}

	@Override
	public void trace(Object msg, Throwable t) {
		trace(msg);
		trace(t);
	}

	@Override
	public void warn(Object msg) {
		log("[WARN] ", msg);
	}

	@Override
	public void warn(Throwable t) {
		log("[WARN] ", t);
	}

	@Override
	public void warn(Object msg, Throwable t) {
		warn(msg);
		warn(t);
	}

	@Override
	public boolean isDebug() {
		return System.getenv("DEBUG") != null;
	}

	@Override
	public boolean isError() {
		return false;
	}

	@Override
	public boolean isInfo() {
		return true;
	}

	@Override
	public boolean isTrace() {
		return false;
	}

	@Override
	public boolean isWarn() {
		return false;
	}

	// -- Service methods --

	@Override
	public void initialize() {
		// HACK: Dirty, because every time a new ImageJ context is created with a
		// StderrLogService, it will "steal" the default exception handling.
		DefaultUncaughtExceptionHandler.install(this);
	}

	// -- private helper methods

	/**
	 * Prints a message to stderr.
	 * 
	 * @param prefix the prefix (can be an empty string)
	 * @param message the message
	 */
	private void log(final String prefix, final Object message) {
		System.err.print(prefix);
		System.err.println(message);
	}

	/**
	 * Prints an exception to stderr.
	 * 
	 * @param prefix the prefix (can be an empty string)
	 * @param t the exception
	 */
	private void log(final String prefix, final Throwable t) {
		System.err.print(prefix);
		t.printStackTrace();
	}

}
