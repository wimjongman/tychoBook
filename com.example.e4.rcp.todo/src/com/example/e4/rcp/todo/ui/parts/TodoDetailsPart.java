package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoDetailsPart {

	@Inject
	MDirtyable dirty;

	private Text summary;
	private Text description;
	private Button btnDone;
	private DateTime dateTime;
	private DataBindingContext ctx = new DataBindingContext();

	// Define listener for the databinding
	IChangeListener listener = new IChangeListener() {
		@Override
		public void handleChange(ChangeEvent event) {
			if (dirty!=null){
				dirty.setDirty(true);
			}
		}
	};
	private Todo todo;

	@PostConstruct
	public void createControls(Composite parent) {

		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginRight = 10;
		gl_parent.marginLeft = 10;
		gl_parent.horizontalSpacing = 10;
		gl_parent.marginWidth = 0;
		parent.setLayout(gl_parent);

		Label lblSummary = new Label(parent, SWT.NONE);
		lblSummary.setText("Summary");

		summary = new Text(parent, SWT.BORDER);
		summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setText("Description");

		description = new Text(parent, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.heightHint = 122;
		description.setLayoutData(gd);

		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("Due Date");

		dateTime = new DateTime(parent, SWT.BORDER);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		new Label(parent, SWT.NONE);

		btnDone = new Button(parent, SWT.CHECK);
		btnDone.setText("Done");
	}

	@Persist
	public void save(MDirtyable dirty, ITodoModel model) {
		model.saveTodo(todo);
		dirty.setDirty(false);
	}

	@Inject
	public void setTodo(
			@Optional 
			@Named(IServiceConstants.ACTIVE_SELECTION) 
			Todo todo) {
		if (todo != null) {
			// Remember the todo as field
			this.todo = todo;
			// update the user interface
			updateUserInterface(todo);
		}
	}
	

	private void updateUserInterface(Todo todo) {
		
		// Check if Ui is available
		// Assumes that one of your fields
		// is called summary
		
		if (summary != null && !summary.isDisposed()) {
			
			// New todo not dirty
			if (dirty!=null){
				dirty.setDirty(false);
			}
			
			// Deregister change listener to the old binding
			IObservableList providers = ctx.getValidationStatusProviders();
			for (Object o : providers) {
				Binding b = (Binding) o;
				b.getTarget().removeChangeListener(listener);
			}
			
			// Remove bindings
			ctx.dispose();
			
			IObservableValue target = WidgetProperties.text(SWT.Modify)
					.observe(summary);
			IObservableValue model = PojoProperties.value(Todo.FIELD_SUMMARY).observe(
					todo);
			ctx.bindValue(target, model);

			target = WidgetProperties.text(SWT.Modify).observe(description);
			model = PojoProperties.value(Todo.FIELD_DESCRIPTION).observe(todo);
			ctx.bindValue(target, model);
			
			target = WidgetProperties.selection().observe(btnDone);
			model = PojoProperties.value(Todo.FIELD_DONE).observe(todo);
			ctx.bindValue(target, model);

			IObservableValue observeSelectionDateTimeObserveWidget = WidgetProperties
					.selection().observe(dateTime);
			IObservableValue dueDateTodoObserveValue = PojoProperties.value(
					Todo.FIELD_DUEDATE).observe(todo);
			ctx.bindValue(observeSelectionDateTimeObserveWidget,
					dueDateTodoObserveValue, null, null);
			
			// Register for the changes
			providers = ctx.getValidationStatusProviders();
			for (Object o : providers) {
				Binding b = (Binding) o;
				b.getTarget().addChangeListener(listener);
			}
		}
	}

	@Focus
	public void onFocus() {
		// The following assumes that you have a Text field
		// called summary
		summary.setFocus();
	}
}
