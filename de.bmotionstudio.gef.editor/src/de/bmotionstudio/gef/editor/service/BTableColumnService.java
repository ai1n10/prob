package de.bmotionstudio.gef.editor.service;

import de.bmotionstudio.gef.editor.AbstractBControlService;
import de.bmotionstudio.gef.editor.IBControlService;
import de.bmotionstudio.gef.editor.model.BControl;
import de.bmotionstudio.gef.editor.model.BTableColumn;
import de.bmotionstudio.gef.editor.model.Visualization;
import de.bmotionstudio.gef.editor.part.AppAbstractEditPart;
import de.bmotionstudio.gef.editor.part.BTableColumnPart;

public class BTableColumnService extends AbstractBControlService implements
		IBControlService {

	@Override
	public BControl createControl(Visualization visualization) {
		return new BTableColumn(visualization);
	}

	@Override
	public AppAbstractEditPart createEditPart() {
		return new BTableColumnPart();
	}

	@Override
	public boolean showInPalette() {
		return false;
	}

}