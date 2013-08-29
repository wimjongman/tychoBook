package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class PlaygroundPart {
	@Inject
	@Preference(nodePath = "com.example.e4.rcp.todo", value = "user")
	String userTodo;
	private Text text2;
	private Text text1;

	@PostConstruct
	public void createControls(Composite parent) {
		DataBindingContext ctx = new DataBindingContext();
		text1 = new Text(parent, SWT.LEAD | SWT.BORDER| SWT.MULTI);
		
		text1.setText("Hallo");
	
		text2 = new Text(parent, SWT.LEAD | SWT.BORDER| SWT.MULTI);
		text2.setText("Moin");
		
		Button button = new Button(parent, SWT.CHECK);
		button.setText("Hello");
//		button.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//			}
//		});
		
		ISWTObservableValue model1 = WidgetProperties.selection().observe(button);
		ISWTObservableValue target1 = WidgetProperties.enabled().observe(text1);
		ctx.bindValue(target1, model1);
		
		// This assumes you have a vogella.png file
		// in folder images
	}

	@Focus
	public void setFocus() {
		text1.setFocus();
	}

	@Inject
	@Optional
	public void trackUserSettings(
			@Preference(nodePath = "com.example.e4.rcp.todo", value = "user") String user) {
		System.out.println("New user: " + user);
		System.out.println("Field: " + userTodo);
	}

	@Inject
	@Optional
	public void trackPasswordSettings(
			@Preference(nodePath = "com.example.e4.rcp.todo", value = "password") String password) {
		System.out.println("New password: " + password);
	}

}
