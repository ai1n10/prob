package de.bmotionstudio.gef.editor.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.RGB;

import de.bmotionstudio.gef.editor.AttributeConstants;
import de.bmotionstudio.gef.editor.BMotionStudioImage;
import de.bmotionstudio.gef.editor.EditorImageRegistry;
import de.bmotionstudio.gef.editor.command.CreateCommand;
import de.bmotionstudio.gef.editor.editpolicy.BMSConnectionEditPolicy;
import de.bmotionstudio.gef.editor.editpolicy.BMSDeletePolicy;
import de.bmotionstudio.gef.editor.editpolicy.TableLayoutEditPolicy;
import de.bmotionstudio.gef.editor.figure.TableFigure;
import de.bmotionstudio.gef.editor.model.BControl;
import de.bmotionstudio.gef.editor.model.BTableCell;
import de.bmotionstudio.gef.editor.model.BTableColumn;

public class BTablePart extends BMSAbstractEditPart {

	@Override
	protected IFigure createEditFigure() {
		TableFigure tableFigure = new TableFigure();
		Label figure = new Label();
		tableFigure.add(figure);
		if (!isRunning()) {
			figure.setIcon(BMotionStudioImage
					.getImage(EditorImageRegistry.IMG_ICON_TR_LEFT));
		}
		return tableFigure;
	}

	@Override
	protected void prepareEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new TableLayoutEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new BMSDeletePolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new BMSConnectionEditPolicy());
	}

	@Override
	protected void prepareRunPolicies() {
	}

	@Override
	protected void refreshEditLayout(IFigure figure, BControl control) {

		int width = 0;

		int rows = Integer.valueOf(control.getAttributeValue(
				AttributeConstants.ATTRIBUTE_ROWS).toString());

		for (BControl column : control.getChildrenArray()) {
			width = width + column.getLayout().width;
		}

		// Set the correct size of the table
		figure.getParent().setConstraint(
				figure,
				new Rectangle(control.getLocation().x, control.getLocation().y,
						width + 21, (rows * 20) + 15));

		// super.refreshEditLayout(figure, control);

	}

	@Override
	public void refreshEditFigure(IFigure figure, BControl model,
			PropertyChangeEvent evt) {

		Object value = evt.getNewValue();
		Object oldValue = evt.getOldValue();
		String aID = evt.getPropertyName();

		if (aID.equals(AttributeConstants.ATTRIBUTE_VISIBLE))
			((TableFigure) figure)
					.setVisible(Boolean.valueOf(value.toString()));

		if (aID.equals(AttributeConstants.ATTRIBUTE_FOREGROUND_COLOR)) {
			((TableFigure) figure).setForegroundColor((RGB) value);
			for (BControl child : model.getChildrenArray())
				child.setAttributeValue(
						AttributeConstants.ATTRIBUTE_FOREGROUND_COLOR, value);
		}

		if (aID.equals(AttributeConstants.ATTRIBUTE_COLUMNS)) {

			if (oldValue == null || value.equals(oldValue))
				return;

			// Create columns
			Integer numberOfColumns = Integer.valueOf(value.toString());
			Integer numberOfCurrentColumns = Integer.valueOf(oldValue
					.toString());

			if (numberOfColumns < numberOfCurrentColumns) {
				for (int i = numberOfCurrentColumns - 1; i >= numberOfColumns; i--) {
					model.removeChild(i);
				}
			}

			for (int i = numberOfCurrentColumns; i < numberOfColumns; i++) {
				BTableColumn bTableColumn = new BTableColumn(
						model.getVisualization());
				CreateCommand cmd = new CreateCommand(bTableColumn, model);
				// cmd.setLayout(new Rectangle(0, 0, 50, 40));
				cmd.execute();
				Integer numberOfRows = Integer.valueOf(model.getAttributeValue(
						AttributeConstants.ATTRIBUTE_ROWS).toString());
				refreshRows(bTableColumn, numberOfRows);
			}

			refreshEditLayout(figure, model);

		}

		if (aID.equals(AttributeConstants.ATTRIBUTE_ROWS)) {

			if (oldValue == null || value.equals(oldValue))
				return;

			// Create rows
			
			Integer numberOfRows = Integer.valueOf(value.toString());

			List<BControl> columnChildren = model.getChildrenArray();
			for (BControl column : columnChildren) {
				refreshRows(column, numberOfRows);
			}
			
			refreshEditLayout(figure, model);

		}

	}

	private void refreshRows(BControl column, int numberOfRows) {
		Integer numberOfCurrentRows = column.getChildrenArray().size();
		if (numberOfRows < numberOfCurrentRows) {
			for (int i = numberOfCurrentRows - 1; i >= numberOfRows; i--) {
				column.removeChild(i);
			}
		}
		for (int i = numberOfCurrentRows; i < numberOfRows; i++) {
			CreateCommand cmd = new CreateCommand(new BTableCell(
					column.getVisualization()), column);
			cmd.setLayout(new Rectangle(0, 0, column.getDimension().width, 20));
			cmd.execute();
		}
	}

	@Override
	public List<BControl> getModelChildren() {
		return ((BControl) getModel()).getChildrenArray();
	}

}
