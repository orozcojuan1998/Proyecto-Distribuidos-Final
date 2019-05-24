package distribuidos;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class UsuarioGUI {

	private JFrame frame;
	private JTextField tarjeta;
	private Vector<Vector<Number>> rowData;
	private String[] nombreColumnas = {"Nombre","Precio", "Cantidad"};
	private Vector<String> columnas;
	private JTable tbProductos;
	/**
	 * Launch the application.
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		RMIInterface control = (RMIInterface)Naming.lookup("rmi://localhost:1900" + "/distribuidos");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UsuarioGUI window = new UsuarioGUI(control);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param control 
	 */
	public UsuarioGUI(RMIInterface control) {
		initialize(control);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(RMIInterface control) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Comprar");
		btnNewButton.setBounds(335, 227, 89, 23);
		panel.add(btnNewButton);
		panel.setVisible(false);
		
		
		
		JLabel lblNoTarjeta = new JLabel("No. Tarjeta:");
		lblNoTarjeta.setBounds(46, 72, 63, 14);
		frame.getContentPane().add(lblNoTarjeta);
		
		tarjeta = new JTextField();
		tarjeta.setBounds(113, 70, 104, 20);
		frame.getContentPane().add(tarjeta);
		tarjeta.setColumns(10);
		
		JButton btnIngresar = new JButton("Ingresar");
		
		btnIngresar.setBounds(113, 158, 89, 23);
		frame.getContentPane().add(btnIngresar);
		
		JPanel pagoRealizado = new JPanel();
		pagoRealizado.setBounds(0, 0, 434, 261);
		frame.getContentPane().add(pagoRealizado);
		pagoRealizado.setLayout(null);
		
		JLabel compraResultado = new JLabel("");
		compraResultado.setBounds(0, 0, 220, 17);
		pagoRealizado.add(compraResultado);
		pagoRealizado.setVisible(false);
		
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean respuesta = false;
				String tarjetaNum = tarjeta.getText();
				try {
					respuesta = control.login(tarjetaNum);
				} catch (RemoteException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if (respuesta) {
					lblNoTarjeta.setVisible(false);
					tarjeta.setVisible(false);
					btnIngresar.setVisible(false);
					panel.setVisible(true);
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(42, 182, 580, 136);
					panel.add(scrollPane);
					rowData.removeAllElements();
					ArrayList<Producto> productos = new ArrayList<Producto>();
					try {
						productos = control.getProducts();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for (Producto p: productos) {
						Vector fila = new Vector();
						fila.add(p.getNombre());
						fila.add(p.getPrecio());
						fila.add(p.getCantidadDisponible());
						rowData.add(fila);	
					}
					tbProductos = new JTable(rowData,columnas);
			        scrollPane.setViewportView(getDatosProductos());
				}
				
			}
		});
		
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Producto> productos = new ArrayList<Producto>();
				if(tbProductos.getSelectedRows().length > -1) {
					int[] selectedrows = tbProductos.getSelectedRows();
					for (int i = 0; i < selectedrows.length; i++) {
						Producto p = new Producto();
						p.setNombre((String) tbProductos.getValueAt(selectedrows[i], 0));
						p.setPrecio((float) tbProductos.getValueAt(selectedrows[i], 1));
						p.setCantidadDisponible((int) tbProductos.getValueAt(selectedrows[i], 2));
						productos.add(p);
					}
				}
				try {
					boolean flag;
					flag = control.check_out(productos);
					if(flag) {
						panel.setVisible(false);
						pagoRealizado.setVisible(true);
						
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 jTable.setRowSelectionAllowed(true);
	 jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	 
	 
	 if (jTable.getSelectedRows() > -1) {

                 int[] selectedrows = jTable.getSelectedRows();

                 for (int i = 0; i < selectedrows.length; i++)
                {

                     System.out.println(jTable.getValueAt(selectedrows[i], 0).toString());

                }

            }
	 */
	
	public JTable getDatosProductos()
	{
		if(tbProductos == null)
		{
			rowData = new Vector<Vector<Number>>();
			columnas = new Vector<String>(Arrays.asList(this.nombreColumnas));
			tbProductos = new JTable(rowData, columnas);
			tbProductos.setForeground(new Color(255, 0, 0));
			tbProductos.setBorder(new LineBorder(new Color(0, 0, 0)));
			tbProductos.setBackground(new Color(255, 0, 0));
			tbProductos.setForeground(Color.BLUE);
			tbProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tbProductos.setCellSelectionEnabled(true);
			tbProductos.setColumnSelectionAllowed(true);
			tbProductos.getTableHeader().setReorderingAllowed(false);
			tbProductos.getTableHeader().setResizingAllowed(false);
			tbProductos.setRowSelectionAllowed(true);
			tbProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return tbProductos;
	}
	
}
	
