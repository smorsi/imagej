//
// AxisPositionHandler.java
//

/*
ImageJ software for multidimensional image processing and analysis.

Copyright (c) 2010, ImageJDev.org.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the names of the ImageJDev.org developers nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package imagej.core.tools;

import imagej.data.display.ImageDisplay;
import imagej.ext.InputModifiers;
import imagej.ext.KeyCode;
import imagej.ext.display.Display;
import imagej.ext.display.event.input.KyPressedEvent;
import imagej.ext.display.event.input.MsWheelEvent;
import imagej.ext.plugin.Plugin;
import imagej.ext.tool.AbstractTool;
import imagej.ext.tool.Tool;
import net.imglib2.meta.Axes;
import net.imglib2.meta.AxisType;

/**
 * Handles keyboard and mouse wheel operations that change the dimensional
 * position.
 * 
 * @author Grant Harris
 */
@Tool(name = "Axis Position Shortcuts", alwaysActive = true,
	activeInAppFrame = true, priority = Plugin.NORMAL_PRIORITY)
public class AxisPositionHandler extends AbstractTool {

	@Override
	public void onKeyDown(final KyPressedEvent evt) {
		final Display<?> display = evt.getDisplay();
		if (!(display instanceof ImageDisplay)) return;
		final ImageDisplay imageDisplay = (ImageDisplay) display;

		final KeyCode keyCode = evt.getCode();
		final char keyChar = evt.getCharacter();

		// determine direction to move based on key press
		final int increment;
		if (keyCode == KeyCode.PERIOD || keyCode == KeyCode.GREATER ||
			keyCode == KeyCode.KP_RIGHT || keyCode == KeyCode.RIGHT || keyChar == '>')
		{
			increment = 1;
		}
		else if (keyCode == KeyCode.COMMA || keyCode == KeyCode.LESS ||
			keyCode == KeyCode.KP_LEFT || keyCode == KeyCode.LEFT || keyChar == '<')
		{
			increment = -1;
		}
		else return; // inapplicable key

		final AxisType axis = getAxis(imageDisplay, evt.getModifiers());
		if (axis == null) return;

		final long pos = imageDisplay.getAxisPosition(axis) + increment;
		imageDisplay.setAxisPosition(axis, pos);
		evt.consume();
	}

	@Override
	public void onMouseWheel(final MsWheelEvent evt) {
		final Display<?> display = evt.getDisplay();
		if (!(display instanceof ImageDisplay)) return;
		final ImageDisplay imageDisplay = (ImageDisplay) display;

		final AxisType axis = getAxis(imageDisplay, evt.getModifiers());
		if (axis == null) return;

		final int rotation = evt.getWheelRotation();

		final long pos = imageDisplay.getAxisPosition(axis) + rotation;
		imageDisplay.setAxisPosition(axis, pos);
		evt.consume();
	}

	// -- Helper methods --

	/**
	 * Determines the axis to move based on the keyboard modifiers used.
	 * <ul>
	 * <li>No modifier moves channel axis</li>
	 * <li>Ctrl modifier moves Z axis</li>
	 * <li>Alt modifier moves Time axis</li>
	 * <li>If preferred axis does not exist, first avilable axis is used</li>
	 * </ul>
	 */
	private AxisType
		getAxis(final ImageDisplay display, final InputModifiers mods)
	{
		if (display.numDimensions() < 3) return null;

		// determine preferred axis based on keyboard modifier used
		final AxisType axis;
		if (mods.isAltDown()) axis = Axes.TIME;
		else if (mods.isCtrlDown() || mods.isMetaDown()) axis = Axes.Z;
		else axis = Axes.CHANNEL;

		if (display.getAxisIndex(axis) < 0) {
			// preferred axis does not exist; return first available axis instead
			return display.axis(2);
		}
		return axis;
	}

}