package Vista;

public class FrmFabrica extends javax.swing.JFrame {

    // ===================== ATRIBUTOS =====================
    private datos.Juego juego;          // controla el avance entre las 3 fábricas
    private datos.Fabrica fabrica;      // la fábrica que se está jugando
    private int posicionSeleccionada = -1; // material seleccionado en la cinta (-1 = ninguno)
    private int materialesDesechados = 0;

    // ===================== COLORES =====================
    private static final java.awt.Color COLOR_GRIS = new java.awt.Color(44, 62, 80);
    private static final java.awt.Color COLOR_TEXTO2 = new java.awt.Color(149, 165, 166);
    private static final java.awt.Color COLOR_FONDO = new java.awt.Color(15, 25, 35);
    private static final java.awt.Color COLOR_NARANJA = new java.awt.Color(243, 156, 18);
    private static final java.awt.Color COLOR_VERDE = new java.awt.Color(46, 204, 113);

    // ===================== CONSTRUCTOR =====================
    /**
     * Crea la ventana de la fábrica.
     *
     * @param numeroFabrica 1, 2 o 3. Define el objetivo y la capacidad de la
     * cinta.
     */
    public FrmFabrica(datos.Juego juego) {
        initComponents();
        this.juego = juego;
        this.fabrica = juego.getFabricaActual();

        // 1. Modelos de la tabla y el combo (sin arreglos)
        configurarComponentes();

        // 2. Estilo del encabezado de la tabla
        pbProgreso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        javax.swing.table.DefaultTableCellRenderer encabezado = new javax.swing.table.DefaultTableCellRenderer();
        encabezado.setBackground(new java.awt.Color(44, 62, 80));
        encabezado.setForeground(new java.awt.Color(236, 240, 241));
        encabezado.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        encabezado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tblPedidos.getTableHeader().setDefaultRenderer(encabezado);
        tblPedidos.getTableHeader().setReorderingAllowed(false);
        scrPedidos.getViewport().setBackground(new java.awt.Color(15, 25, 35));

        // 3. Distribución de la cinta: una fila, columnas automáticas
        pnlSlots.setLayout(new java.awt.GridLayout(1, 0, 12, 0));
        pnlSlots.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 4. Datos del juego
        configurarFabrica();
        refrescarTodo();

        // 5. Tamaño y posición de la ventana (siempre al final)
        pnlFondo.setPreferredSize(new java.awt.Dimension(1280, 740));
        pack();
        setLocationRelativeTo(null);
    }

    // ===================== CONFIGURACIÓN =====================
    /**
     * Crea las columnas de la tabla y las opciones del combo sin usar arreglos.
     */
    private void configurarComponentes() {
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false; // la tabla es solo de lectura
            }
        };
        modelo.addColumn("Pedido");
        modelo.addColumn("Automóvil");
        modelo.addColumn("Ganancia");
        modelo.addColumn("Estado");
        tblPedidos.setModel(modelo);

        // Ancho de las columnas (el nombre del auto es el más largo)
        tblPedidos.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblPedidos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblPedidos.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblPedidos.getColumnModel().getColumn(3).setPreferredWidth(90);

        cmbLinea.addItem("Línea 1");
        cmbLinea.addItem("Línea 2");
        cmbLinea.addItem("Línea 3");
    }

    // Ajusta los textos del HUD según la fábrica actual.
    private void configurarFabrica() {
        lblFabrica.setText("FÁBRICA " + fabrica.getNumeroFabrica() + " / 3");
        lblObjetivo.setText(formatearDinero(fabrica.getObjetivoEconomico()));
        lblMensaje.setText("   Bienvenido a la Fábrica " + fabrica.getNumeroFabrica()
                + ". Asigná un pedido para empezar.");
    }

    /**
     * Convierte un monto a texto con formato $90.000
     */
    private String formatearDinero(double monto) {
        java.text.DecimalFormatSymbols simbolos = new java.text.DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.');
        java.text.DecimalFormat formato = new java.text.DecimalFormat("$#,##0", simbolos);
        return formato.format(monto);
    }

    /**
     * Recorre la lista enlazada de pedidos y la muestra en la tabla.
     */
    private void actualizarTablaPedidos() {
        javax.swing.table.DefaultTableModel modelo
                = (javax.swing.table.DefaultTableModel) tblPedidos.getModel();
        modelo.setRowCount(fabrica.getPedidos().tamano());

        Estructuras.NodoPedido actual = fabrica.getPedidos().getCabeza();
        int fila = 0;
        while (actual != null) {
            datos.pedido p = actual.getDato();
            modelo.setValueAt(p.getIdentificador(), fila, 0);
            modelo.setValueAt(p.getTipoAutomovil(), fila, 1);
            modelo.setValueAt(formatearDinero(p.getTipo().getGanancia()), fila, 2);
            modelo.setValueAt(p.getEstado(), fila, 3);
            actual = actual.getSiguiente();
            fila++;
        }
    }

    // ===================== CINTA TRANSPORTADORA =====================
    /**
     * Vuelve a dibujar los materiales de la cinta recorriendo su lista
     * enlazada.
     */
    private void refrescarCinta() {
        datos.CintaTransportadora cinta = fabrica.getCinta();
        pnlSlots.removeAll();

        Estructuras.Nodo actual = cinta.getCabeza();
        int posicion = 0;
        while (actual != null) {
            pnlSlots.add(crearBotonMaterial(actual.getDato(), posicion));
            actual = actual.getSiguiente();
            posicion++;
        }

        lblCintaCapacidad.setText(cinta.getCantidadActual() + " / "
                + cinta.getCapacidadMaxima() + " espacios");
        pnlSlots.revalidate();
        pnlSlots.repaint();
    }

    /**
     * Crea el botón que representa un material de la cinta.
     */
    private javax.swing.JButton crearBotonMaterial(datos.material m, int posicion) {
        javax.swing.JButton btn = new javax.swing.JButton(
                "<html><center>" + m.getTipo().getAuto()
                + "<br><br>" + formatearDinero(m.getTipo().getValor())
                + "</center></html>");
        btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btn.setForeground(new java.awt.Color(236, 240, 241));
        if (posicion == posicionSeleccionada) {
            btn.setBackground(new java.awt.Color(52, 152, 219));   // azul = seleccionado
        } else {
            btn.setBackground(new java.awt.Color(44, 62, 80));     // gris = normal
        }
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.addActionListener(e -> seleccionarMaterial(posicion));
        return btn;
    }

    /**
     * Busca el material que está en una posición de la cinta.
     */
    private datos.material obtenerMaterialCinta(int posicion) {
        Estructuras.Nodo actual = fabrica.getCinta().getCabeza();
        int i = 0;
        while (actual != null) {
            if (i == posicion) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
            i++;
        }
        return null;
    }

    /**
     * Marca un material como seleccionado y muestra su información en el
     * basurero.
     */
    private void seleccionarMaterial(int posicion) {
        posicionSeleccionada = posicion;
        datos.material m = obtenerMaterialCinta(posicion);

        lblBasMaterial.setText(m.getTipo().getAuto());
        lblBasPerdida.setText("Pérdida si se desecha: "
                + formatearDinero(m.getTipo().calcularPerdida()));
        mostrarMensaje(m.getTipo().getAuto()
                + " seleccionado. Ponelo en una línea o desechalo.");

        refrescarCinta();   // para que el seleccionado se vea azul
    }

    /**
     * Muestra un mensaje en la barra inferior (reemplaza a JOptionPane).
     */
    private void mostrarMensaje(String texto) {
        lblMensaje.setText("   " + texto);
    }

    // ===================== REFRESCAR PANTALLA =====================
    /**
     * Actualiza toda la pantalla según el estado actual de la fábrica.
     */
    private void refrescarTodo() {
        actualizarTablaPedidos();
        refrescarCinta();
        refrescarLinea(fabrica.getLinea1(), lblL1Estado, lblL1Pedido, lblL1Ganancia,
                lblL1Mat1, lblL1Mat2, lblL1Mat3, btnL1Construir);
        refrescarLinea(fabrica.getLinea2(), lblL2Estado, lblL2Pedido, lblL2Ganancia,
                lblL2Mat1, lblL2Mat2, lblL2Mat3, btnL2Construir);
        refrescarLinea(fabrica.getLinea3(), lblL3Estado, lblL3Pedido, lblL3Ganancia,
                lblL3Mat1, lblL3Mat2, lblL3Mat3, btnL3Construir);
        refrescarHud();
    }

    /**
     * Actualiza dinero, pérdidas y barra de progreso.
     */
    private void refrescarHud() {
        double dinero = fabrica.getDineroAcumulado();
        lblDinero.setText(formatearDinero(dinero));
        lblPerdidas.setText(formatearDinero(fabrica.getBasurero().getPerdidasAcumuladas()));

        int progreso = (int) (dinero * 100 / fabrica.getObjetivoEconomico());
        if (progreso < 0) {
            progreso = 0;
        }
        if (progreso > 100) {
            progreso = 100;
        }
        pbProgreso.setValue(progreso);
    }

    /**
     * Muestra en la tarjeta de una línea su estado, pedido y materiales.
     */
    private void refrescarLinea(datos.lineaProduccion linea, javax.swing.JLabel lblEstado,
            javax.swing.JLabel lblPedido, javax.swing.JLabel lblGanancia,
            javax.swing.JLabel lblMat1, javax.swing.JLabel lblMat2, javax.swing.JLabel lblMat3,
            javax.swing.JButton btnConstruir) {

        // Línea libre
        if (linea.estaLibre()) {
            pintarEstado(lblEstado, "DISPONIBLE", COLOR_GRIS, COLOR_TEXTO2);
            lblPedido.setText("Sin pedido asignado");
            lblGanancia.setText("");
            lblMat1.setText("—");
            lblMat1.setForeground(COLOR_TEXTO2);
            lblMat2.setText("—");
            lblMat2.setForeground(COLOR_TEXTO2);
            lblMat3.setText("—");
            lblMat3.setForeground(COLOR_TEXTO2);
            btnConstruir.setEnabled(false);
            btnConstruir.setBackground(COLOR_GRIS);
            return;
        }

        // Línea con pedido
        datos.pedido p = linea.getPedidoActual();
        datos.tipoAuto auto = p.getTipo();
        lblPedido.setText(p.getIdentificador() + " · " + p.getTipoAutomovil());
        lblPedido.setToolTipText(p.getTipoAutomovil());   // nombre completo al pasar el mouse
        lblGanancia.setText("+" + formatearDinero(auto.getGanancia()));

        mostrarMaterial(lblMat1, auto.getMaterialRequerido1(), linea);
        mostrarMaterial(lblMat2, auto.getMaterialRequerido2(), linea);
        mostrarMaterial(lblMat3, auto.getMaterialRequerido3(), linea);

        if (linea.tieneMaterialesNecesarios(auto)) {
            pintarEstado(lblEstado, "LISTA", COLOR_VERDE, COLOR_FONDO);
            btnConstruir.setEnabled(true);
            btnConstruir.setBackground(COLOR_VERDE);
        } else {
            pintarEstado(lblEstado, "OCUPADA", COLOR_NARANJA, COLOR_FONDO);
            btnConstruir.setEnabled(false);
            btnConstruir.setBackground(COLOR_GRIS);
        }
    }

    /**
     * Muestra un material requerido: ✔ verde si ya está en la línea, ○ gris si
     * falta.
     */
    private void mostrarMaterial(javax.swing.JLabel lbl, String nombre, datos.lineaProduccion linea) {
        if (nombre == null) {          // los autos de 2 materiales no usan el tercero
            lbl.setText("");
            return;
        }
        if (linea.getMaterialesRecibidos().contieneMaterial(nombre)) {
            lbl.setText("✔  " + nombre);
            lbl.setForeground(COLOR_VERDE);
        } else {
            lbl.setText("○  " + nombre);
            lbl.setForeground(COLOR_TEXTO2);
        }
    }

    /**
     * Cambia el texto y los colores de la etiqueta de estado de una línea.
     */
    private void pintarEstado(javax.swing.JLabel lbl, String texto,
            java.awt.Color fondo, java.awt.Color letra) {
        lbl.setText(texto);
        lbl.setBackground(fondo);
        lbl.setForeground(letra);
    }

    /**
     * Devuelve la línea de producción según su número (1, 2 o 3).
     */
    private datos.lineaProduccion obtenerLinea(int numeroLinea) {
        switch (numeroLinea) {
            case 1:
                return fabrica.getLinea1();
            case 2:
                return fabrica.getLinea2();
            default:
                return fabrica.getLinea3();
        }
    }

    /**
     * Quita la selección de material y limpia el basurero.
     */
    private void limpiarSeleccion() {
        posicionSeleccionada = -1;
        lblBasMaterial.setText("Ninguno");
        lblBasPerdida.setText("Pérdida si se desecha: $0");
    }

    /**
     * Pone el material seleccionado de la cinta en una línea.
     */
    private void ponerMaterial(int numeroLinea) {
        if (posicionSeleccionada == -1) {
            mostrarMensaje("Primero seleccioná un material de la cinta.");
            return;
        }
        if (obtenerLinea(numeroLinea).estaLibre()) {
            mostrarMensaje("La Línea " + numeroLinea + " no tiene pedido. Asigná uno primero.");
            return;
        }

        String nombre = obtenerMaterialCinta(posicionSeleccionada).getTipo().getAuto();

        if (fabrica.agregarMaterialALinea(posicionSeleccionada, numeroLinea)) {
            mostrarMensaje(nombre + " colocado en la Línea " + numeroLinea + ".");
        } else {
            mostrarMensaje(nombre + " no sirve para el auto de la Línea " + numeroLinea
                    + " o ya lo tiene. Probá en otra línea o desechalo.");
        }
        limpiarSeleccion();
        refrescarTodo();
    }

    /**
     * Construye el auto de una línea y suma la ganancia.
     */
    private void construir(int numeroLinea) {
        datos.pedido p = obtenerLinea(numeroLinea).getPedidoActual();
        if (p == null) {
            return;
        }
        double ganancia = p.getTipo().getGanancia();

        if (fabrica.completarConstruccion(numeroLinea)) {
            mostrarMensaje("¡" + p.getTipoAutomovil() + " construido en la Línea " + numeroLinea
                    + "! Ganaste " + formatearDinero(ganancia) + ".");
        } else {
            mostrarMensaje("A la Línea " + numeroLinea + " todavía le faltan materiales.");
        }
        refrescarTodo();
        verificarObjetivo();
    }

    /**
     * Cancela el pedido de una línea.
     */
    private void cancelar(int numeroLinea) {
        if (fabrica.cancelarPedido(numeroLinea)) {
            mostrarMensaje("Pedido de la Línea " + numeroLinea
                    + " cancelado. Volvió a la lista y se perdieron sus materiales.");
        } else {
            mostrarMensaje("La Línea " + numeroLinea + " no tiene pedido para cancelar.");
        }
        refrescarTodo();
    }

    /**
     * Revisa si ya se alcanzó el objetivo de la fábrica.
     */
    private void verificarObjetivo() {
        if (fabrica.objetivoAlcanzado()) {
            mostrarMensaje("¡Objetivo cumplido! Superaste la Fábrica "
                    + fabrica.getNumeroFabrica() + ".");
            // Etapa 6: abrir la jerarquía y pasar a la siguiente fábrica
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFondo = new javax.swing.JPanel();
        pnlHud = new javax.swing.JPanel();
        lblFabrica = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        pnlDinero = new javax.swing.JPanel();
        lblDineroTit = new javax.swing.JLabel();
        lblDinero = new javax.swing.JLabel();
        pnlObjetivo = new javax.swing.JPanel();
        lblObjetivoTit = new javax.swing.JLabel();
        lblObjetivo = new javax.swing.JLabel();
        pnlPerdidas = new javax.swing.JPanel();
        lblPerdidasTit = new javax.swing.JLabel();
        lblPerdidas = new javax.swing.JLabel();
        lblProgresoTit = new javax.swing.JLabel();
        pbProgreso = new javax.swing.JProgressBar();
        pnlHub = new javax.swing.JPanel();
        lblHubTitulo = new javax.swing.JLabel();
        lblHubInfo = new javax.swing.JLabel();
        scrPedidos = new javax.swing.JScrollPane();
        tblPedidos = new javax.swing.JTable();
        cmbLinea = new javax.swing.JComboBox<>();
        btnAsignar = new javax.swing.JButton();
        pnlLinea39 = new javax.swing.JPanel();
        lblL3Nombre = new javax.swing.JLabel();
        lblL3Estado = new javax.swing.JLabel();
        pnlL3Sep = new javax.swing.JPanel();
        lblL3Pedido = new javax.swing.JLabel();
        lblL3Ganancia = new javax.swing.JLabel();
        lblL3MatTit = new javax.swing.JLabel();
        lblL3Mat3 = new javax.swing.JLabel();
        lblL3Mat1 = new javax.swing.JLabel();
        lblL3Mat2 = new javax.swing.JLabel();
        btnL3Poner = new javax.swing.JButton();
        btnL3Construir = new javax.swing.JButton();
        btnL3Cancelar = new javax.swing.JButton();
        pnlLinea1 = new javax.swing.JPanel();
        lblL1Nombre = new javax.swing.JLabel();
        lblL1Estado = new javax.swing.JLabel();
        pnlL1Sep = new javax.swing.JPanel();
        lblL1Pedido = new javax.swing.JLabel();
        lblL1Ganancia = new javax.swing.JLabel();
        lblL1MatTit = new javax.swing.JLabel();
        lblL1Mat3 = new javax.swing.JLabel();
        lblL1Mat1 = new javax.swing.JLabel();
        lblL1Mat2 = new javax.swing.JLabel();
        btnL1Poner = new javax.swing.JButton();
        btnL1Construir = new javax.swing.JButton();
        btnL1Cancelar = new javax.swing.JButton();
        pnlLinea2 = new javax.swing.JPanel();
        lblL2Nombre = new javax.swing.JLabel();
        lblL2Estado = new javax.swing.JLabel();
        pnlL2Sep = new javax.swing.JPanel();
        lblL2Pedido = new javax.swing.JLabel();
        lblL2Ganancia = new javax.swing.JLabel();
        lblL2MatTit = new javax.swing.JLabel();
        lblL2Mat3 = new javax.swing.JLabel();
        lblL2Mat1 = new javax.swing.JLabel();
        lblL2Mat2 = new javax.swing.JLabel();
        btnL2Poner = new javax.swing.JButton();
        btnL2Construir = new javax.swing.JButton();
        btnL2Cancelar = new javax.swing.JButton();
        pnlCinta = new javax.swing.JPanel();
        lblCintaTitulo = new javax.swing.JLabel();
        lblCintaCapacidad = new javax.swing.JLabel();
        pnlSlots = new javax.swing.JPanel();
        pnlBasurero = new javax.swing.JPanel();
        lblBasTit = new javax.swing.JLabel();
        lblBasSelTit = new javax.swing.JLabel();
        lblBasMaterial = new javax.swing.JLabel();
        lblBasPerdida = new javax.swing.JLabel();
        btnDesechar = new javax.swing.JButton();
        lblBasTotal = new javax.swing.JLabel();
        lblMensaje = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CarFactory-Fide · Fábrica ");
        setResizable(false);

        pnlFondo.setBackground(new java.awt.Color(15, 25, 35));
        pnlFondo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnlFondo.setLayout(null);

        pnlHud.setBackground(new java.awt.Color(27, 40, 54));
        pnlHud.setLayout(null);

        lblFabrica.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFabrica.setForeground(new java.awt.Color(149, 165, 166));
        lblFabrica.setText("FÁBRICA 1 / 3");
        pnlHud.add(lblFabrica);
        lblFabrica.setBounds(20, 44, 250, 22);

        lblTitulo.setFont(new java.awt.Font("Segoe UI Black", 0, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(243, 156, 18));
        lblTitulo.setText("CAR FACTORY");
        pnlHud.add(lblTitulo);
        lblTitulo.setBounds(20, 12, 250, 30);

        pnlDinero.setBackground(new java.awt.Color(15, 25, 35));
        pnlDinero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        pnlDinero.setLayout(null);

        lblDineroTit.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDineroTit.setForeground(new java.awt.Color(149, 165, 166));
        lblDineroTit.setText("DINERO");
        pnlDinero.add(lblDineroTit);
        lblDineroTit.setBounds(12, 6, 160, 16);

        lblDinero.setFont(new java.awt.Font("Consolas", 1, 22)); // NOI18N
        lblDinero.setForeground(new java.awt.Color(46, 204, 113));
        lblDinero.setText("$0");
        pnlDinero.add(lblDinero);
        lblDinero.setBounds(12, 22, 160, 30);

        pnlHud.add(pnlDinero);
        pnlDinero.setBounds(420, 12, 180, 56);

        pnlObjetivo.setBackground(new java.awt.Color(15, 25, 35));
        pnlObjetivo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        pnlObjetivo.setLayout(null);

        lblObjetivoTit.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblObjetivoTit.setForeground(new java.awt.Color(149, 165, 166));
        lblObjetivoTit.setText("OBJETIVO");
        pnlObjetivo.add(lblObjetivoTit);
        lblObjetivoTit.setBounds(12, 6, 160, 16);

        lblObjetivo.setFont(new java.awt.Font("Consolas", 1, 22)); // NOI18N
        lblObjetivo.setForeground(new java.awt.Color(236, 240, 241));
        lblObjetivo.setText("$90.000");
        pnlObjetivo.add(lblObjetivo);
        lblObjetivo.setBounds(12, 22, 160, 30);

        pnlHud.add(pnlObjetivo);
        pnlObjetivo.setBounds(615, 12, 180, 56);

        pnlPerdidas.setBackground(new java.awt.Color(15, 25, 35));
        pnlPerdidas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        pnlPerdidas.setLayout(null);

        lblPerdidasTit.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblPerdidasTit.setForeground(new java.awt.Color(149, 165, 166));
        lblPerdidasTit.setText("PÉRDIDAS");
        pnlPerdidas.add(lblPerdidasTit);
        lblPerdidasTit.setBounds(12, 6, 160, 16);

        lblPerdidas.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblPerdidas.setForeground(new java.awt.Color(231, 76, 60));
        lblPerdidas.setText("$0");
        pnlPerdidas.add(lblPerdidas);
        lblPerdidas.setBounds(12, 22, 160, 30);

        pnlHud.add(pnlPerdidas);
        pnlPerdidas.setBounds(810, 12, 180, 56);

        lblProgresoTit.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblProgresoTit.setForeground(new java.awt.Color(149, 165, 166));
        lblProgresoTit.setText("Progreso hacia el objetivo");
        pnlHud.add(lblProgresoTit);
        lblProgresoTit.setBounds(1010, 20, 250, 16);

        pbProgreso.setBackground(new java.awt.Color(15, 25, 35));
        pbProgreso.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        pbProgreso.setForeground(new java.awt.Color(46, 204, 113));
        pbProgreso.setStringPainted(true);
        pnlHud.add(pbProgreso);
        pbProgreso.setBounds(1010, 40, 250, 24);

        pnlFondo.add(pnlHud);
        pnlHud.setBounds(0, 0, 1280, 80);

        pnlHub.setBackground(new java.awt.Color(27, 40, 54));
        pnlHub.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        pnlHub.setLayout(null);

        lblHubTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblHubTitulo.setForeground(new java.awt.Color(243, 156, 18));
        lblHubTitulo.setText("PEDIDOS ENTRANTES");
        pnlHub.add(lblHubTitulo);
        lblHubTitulo.setBounds(15, 10, 370, 24);

        lblHubInfo.setForeground(new java.awt.Color(149, 165, 166));
        lblHubInfo.setText("Elegí un pedido y asignalo a una línea");
        pnlHub.add(lblHubInfo);
        lblHubInfo.setBounds(15, 34, 370, 18);

        tblPedidos.setBackground(new java.awt.Color(15, 25, 35));
        tblPedidos.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblPedidos.setForeground(new java.awt.Color(236, 240, 241));
        tblPedidos.setModel(new javax.swing.table.DefaultTableModel());
        tblPedidos.setGridColor(new java.awt.Color(44, 62, 80));
        tblPedidos.setRowHeight(28);
        tblPedidos.setSelectionBackground(new java.awt.Color(52, 152, 219));
        tblPedidos.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblPedidos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrPedidos.setViewportView(tblPedidos);

        pnlHub.add(scrPedidos);
        scrPedidos.setBounds(15, 60, 370, 210);

        cmbLinea.setBackground(new java.awt.Color(15, 25, 35));
        cmbLinea.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        cmbLinea.setForeground(new java.awt.Color(236, 240, 241));
        cmbLinea.setModel(new javax.swing.DefaultComboBoxModel<>());
        cmbLinea.setFocusable(false);
        cmbLinea.addActionListener(this::cmbLineaActionPerformed);
        pnlHub.add(cmbLinea);
        cmbLinea.setBounds(15, 285, 170, 40);

        btnAsignar.setBackground(new java.awt.Color(52, 152, 219));
        btnAsignar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAsignar.setForeground(new java.awt.Color(255, 255, 255));
        btnAsignar.setText("ASIGNAR");
        btnAsignar.setBorderPainted(false);
        btnAsignar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAsignar.setFocusPainted(false);
        btnAsignar.addActionListener(this::btnAsignarActionPerformed);
        pnlHub.add(btnAsignar);
        btnAsignar.setBounds(200, 285, 185, 40);

        pnlFondo.add(pnlHub);
        pnlHub.setBounds(20, 95, 400, 340);

        pnlLinea39.setBackground(new java.awt.Color(27, 40, 54));
        pnlLinea39.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        pnlLinea39.setLayout(null);

        lblL3Nombre.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        lblL3Nombre.setForeground(new java.awt.Color(236, 240, 241));
        lblL3Nombre.setText("LÍNEA 3");
        pnlLinea39.add(lblL3Nombre);
        lblL3Nombre.setBounds(15, 12, 130, 26);

        lblL3Estado.setBackground(new java.awt.Color(44, 62, 80));
        lblL3Estado.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblL3Estado.setForeground(new java.awt.Color(149, 165, 166));
        lblL3Estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblL3Estado.setText("DISPONIBLE");
        lblL3Estado.setOpaque(true);
        pnlLinea39.add(lblL3Estado);
        lblL3Estado.setBounds(150, 14, 95, 22);

        pnlL3Sep.setBackground(new java.awt.Color(44, 62, 80));

        javax.swing.GroupLayout pnlL3SepLayout = new javax.swing.GroupLayout(pnlL3Sep);
        pnlL3Sep.setLayout(pnlL3SepLayout);
        pnlL3SepLayout.setHorizontalGroup(
            pnlL3SepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        pnlL3SepLayout.setVerticalGroup(
            pnlL3SepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        pnlLinea39.add(pnlL3Sep);
        pnlL3Sep.setBounds(15, 46, 230, 2);

        lblL3Pedido.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblL3Pedido.setForeground(new java.awt.Color(236, 240, 241));
        lblL3Pedido.setText("Sin pedido asignado");
        pnlLinea39.add(lblL3Pedido);
        lblL3Pedido.setBounds(15, 56, 230, 22);

        lblL3Ganancia.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblL3Ganancia.setForeground(new java.awt.Color(46, 204, 113));
        lblL3Ganancia.setText("+$0");
        pnlLinea39.add(lblL3Ganancia);
        lblL3Ganancia.setBounds(15, 76, 230, 18);

        lblL3MatTit.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblL3MatTit.setForeground(new java.awt.Color(149, 165, 166));
        lblL3MatTit.setText("MATERIALES");
        pnlLinea39.add(lblL3MatTit);
        lblL3MatTit.setBounds(15, 105, 230, 16);

        lblL3Mat3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL3Mat3.setForeground(new java.awt.Color(149, 165, 166));
        lblL3Mat3.setText("—");
        pnlLinea39.add(lblL3Mat3);
        lblL3Mat3.setBounds(15, 185, 230, 26);

        lblL3Mat1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL3Mat1.setForeground(new java.awt.Color(149, 165, 166));
        lblL3Mat1.setText("—");
        pnlLinea39.add(lblL3Mat1);
        lblL3Mat1.setBounds(15, 125, 230, 26);

        lblL3Mat2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL3Mat2.setForeground(new java.awt.Color(149, 165, 166));
        lblL3Mat2.setText("—");
        pnlLinea39.add(lblL3Mat2);
        lblL3Mat2.setBounds(15, 155, 230, 26);

        btnL3Poner.setBackground(new java.awt.Color(52, 152, 219));
        btnL3Poner.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnL3Poner.setForeground(new java.awt.Color(255, 255, 255));
        btnL3Poner.setText("PONER MATERIAL");
        btnL3Poner.setBorderPainted(false);
        btnL3Poner.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL3Poner.setFocusPainted(false);
        btnL3Poner.addActionListener(this::btnL3PonerActionPerformed);
        pnlLinea39.add(btnL3Poner);
        btnL3Poner.setBounds(15, 225, 230, 36);

        btnL3Construir.setBackground(new java.awt.Color(44, 62, 80));
        btnL3Construir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnL3Construir.setForeground(new java.awt.Color(15, 25, 35));
        btnL3Construir.setText("CONSTRUIR");
        btnL3Construir.setBorderPainted(false);
        btnL3Construir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL3Construir.setEnabled(false);
        btnL3Construir.setFocusPainted(false);
        btnL3Construir.addActionListener(this::btnL3ConstruirActionPerformed);
        pnlLinea39.add(btnL3Construir);
        btnL3Construir.setBounds(15, 268, 230, 36);

        btnL3Cancelar.setForeground(new java.awt.Color(231, 76, 60));
        btnL3Cancelar.setText("Cancelar pedido");
        btnL3Cancelar.setBorderPainted(false);
        btnL3Cancelar.setContentAreaFilled(false);
        btnL3Cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL3Cancelar.setFocusPainted(false);
        btnL3Cancelar.addActionListener(this::btnL3CancelarActionPerformed);
        pnlLinea39.add(btnL3Cancelar);
        btnL3Cancelar.setBounds(15, 308, 230, 24);

        pnlFondo.add(pnlLinea39);
        pnlLinea39.setBounds(1000, 95, 260, 340);

        pnlLinea1.setBackground(new java.awt.Color(27, 40, 54));
        pnlLinea1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        pnlLinea1.setLayout(null);

        lblL1Nombre.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        lblL1Nombre.setForeground(new java.awt.Color(236, 240, 241));
        lblL1Nombre.setText("LÍNEA 1");
        pnlLinea1.add(lblL1Nombre);
        lblL1Nombre.setBounds(15, 12, 130, 26);

        lblL1Estado.setBackground(new java.awt.Color(44, 62, 80));
        lblL1Estado.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblL1Estado.setForeground(new java.awt.Color(149, 165, 166));
        lblL1Estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblL1Estado.setText("DISPONIBLE");
        lblL1Estado.setOpaque(true);
        pnlLinea1.add(lblL1Estado);
        lblL1Estado.setBounds(150, 14, 95, 22);

        pnlL1Sep.setBackground(new java.awt.Color(44, 62, 80));

        javax.swing.GroupLayout pnlL1SepLayout = new javax.swing.GroupLayout(pnlL1Sep);
        pnlL1Sep.setLayout(pnlL1SepLayout);
        pnlL1SepLayout.setHorizontalGroup(
            pnlL1SepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        pnlL1SepLayout.setVerticalGroup(
            pnlL1SepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        pnlLinea1.add(pnlL1Sep);
        pnlL1Sep.setBounds(15, 46, 230, 2);

        lblL1Pedido.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblL1Pedido.setForeground(new java.awt.Color(236, 240, 241));
        lblL1Pedido.setText("Sin pedido asignado");
        pnlLinea1.add(lblL1Pedido);
        lblL1Pedido.setBounds(15, 56, 230, 22);

        lblL1Ganancia.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblL1Ganancia.setForeground(new java.awt.Color(46, 204, 113));
        lblL1Ganancia.setText("+$0");
        pnlLinea1.add(lblL1Ganancia);
        lblL1Ganancia.setBounds(15, 76, 230, 18);

        lblL1MatTit.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblL1MatTit.setForeground(new java.awt.Color(149, 165, 166));
        lblL1MatTit.setText("MATERIALES");
        pnlLinea1.add(lblL1MatTit);
        lblL1MatTit.setBounds(15, 105, 230, 16);

        lblL1Mat3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL1Mat3.setForeground(new java.awt.Color(149, 165, 166));
        lblL1Mat3.setText("—");
        pnlLinea1.add(lblL1Mat3);
        lblL1Mat3.setBounds(15, 185, 230, 26);

        lblL1Mat1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL1Mat1.setForeground(new java.awt.Color(149, 165, 166));
        lblL1Mat1.setText("—");
        pnlLinea1.add(lblL1Mat1);
        lblL1Mat1.setBounds(15, 125, 230, 26);

        lblL1Mat2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL1Mat2.setForeground(new java.awt.Color(149, 165, 166));
        lblL1Mat2.setText("—");
        pnlLinea1.add(lblL1Mat2);
        lblL1Mat2.setBounds(15, 155, 230, 26);

        btnL1Poner.setBackground(new java.awt.Color(52, 152, 219));
        btnL1Poner.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnL1Poner.setForeground(new java.awt.Color(255, 255, 255));
        btnL1Poner.setText("PONER MATERIAL");
        btnL1Poner.setBorderPainted(false);
        btnL1Poner.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL1Poner.setFocusPainted(false);
        btnL1Poner.addActionListener(this::btnL1PonerActionPerformed);
        pnlLinea1.add(btnL1Poner);
        btnL1Poner.setBounds(15, 225, 230, 36);

        btnL1Construir.setBackground(new java.awt.Color(44, 62, 80));
        btnL1Construir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnL1Construir.setForeground(new java.awt.Color(15, 25, 35));
        btnL1Construir.setText("CONSTRUIR");
        btnL1Construir.setBorderPainted(false);
        btnL1Construir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL1Construir.setEnabled(false);
        btnL1Construir.setFocusPainted(false);
        btnL1Construir.addActionListener(this::btnL1ConstruirActionPerformed);
        pnlLinea1.add(btnL1Construir);
        btnL1Construir.setBounds(15, 268, 230, 36);

        btnL1Cancelar.setForeground(new java.awt.Color(231, 76, 60));
        btnL1Cancelar.setText("Cancelar pedido");
        btnL1Cancelar.setBorderPainted(false);
        btnL1Cancelar.setContentAreaFilled(false);
        btnL1Cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL1Cancelar.setFocusPainted(false);
        btnL1Cancelar.addActionListener(this::btnL1CancelarActionPerformed);
        pnlLinea1.add(btnL1Cancelar);
        btnL1Cancelar.setBounds(15, 308, 230, 24);

        pnlFondo.add(pnlLinea1);
        pnlLinea1.setBounds(440, 95, 260, 340);

        pnlLinea2.setBackground(new java.awt.Color(27, 40, 54));
        pnlLinea2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        pnlLinea2.setLayout(null);

        lblL2Nombre.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        lblL2Nombre.setForeground(new java.awt.Color(236, 240, 241));
        lblL2Nombre.setText("LÍNEA 2");
        pnlLinea2.add(lblL2Nombre);
        lblL2Nombre.setBounds(15, 12, 130, 26);

        lblL2Estado.setBackground(new java.awt.Color(44, 62, 80));
        lblL2Estado.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblL2Estado.setForeground(new java.awt.Color(149, 165, 166));
        lblL2Estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblL2Estado.setText("DISPONIBLE");
        lblL2Estado.setOpaque(true);
        pnlLinea2.add(lblL2Estado);
        lblL2Estado.setBounds(150, 14, 95, 22);

        pnlL2Sep.setBackground(new java.awt.Color(44, 62, 80));

        javax.swing.GroupLayout pnlL2SepLayout = new javax.swing.GroupLayout(pnlL2Sep);
        pnlL2Sep.setLayout(pnlL2SepLayout);
        pnlL2SepLayout.setHorizontalGroup(
            pnlL2SepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        pnlL2SepLayout.setVerticalGroup(
            pnlL2SepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        pnlLinea2.add(pnlL2Sep);
        pnlL2Sep.setBounds(15, 46, 230, 2);

        lblL2Pedido.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblL2Pedido.setForeground(new java.awt.Color(236, 240, 241));
        lblL2Pedido.setText("Sin pedido asignado");
        pnlLinea2.add(lblL2Pedido);
        lblL2Pedido.setBounds(15, 56, 230, 22);

        lblL2Ganancia.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblL2Ganancia.setForeground(new java.awt.Color(46, 204, 113));
        lblL2Ganancia.setText("+$0");
        pnlLinea2.add(lblL2Ganancia);
        lblL2Ganancia.setBounds(15, 76, 230, 18);

        lblL2MatTit.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblL2MatTit.setForeground(new java.awt.Color(149, 165, 166));
        lblL2MatTit.setText("MATERIALES");
        pnlLinea2.add(lblL2MatTit);
        lblL2MatTit.setBounds(15, 105, 230, 16);

        lblL2Mat3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL2Mat3.setForeground(new java.awt.Color(149, 165, 166));
        lblL2Mat3.setText("—");
        pnlLinea2.add(lblL2Mat3);
        lblL2Mat3.setBounds(15, 185, 230, 26);

        lblL2Mat1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL2Mat1.setForeground(new java.awt.Color(149, 165, 166));
        lblL2Mat1.setText("—");
        pnlLinea2.add(lblL2Mat1);
        lblL2Mat1.setBounds(15, 125, 230, 26);

        lblL2Mat2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblL2Mat2.setForeground(new java.awt.Color(149, 165, 166));
        lblL2Mat2.setText("—");
        pnlLinea2.add(lblL2Mat2);
        lblL2Mat2.setBounds(15, 155, 230, 26);

        btnL2Poner.setBackground(new java.awt.Color(52, 152, 219));
        btnL2Poner.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnL2Poner.setForeground(new java.awt.Color(255, 255, 255));
        btnL2Poner.setText("PONER MATERIAL");
        btnL2Poner.setBorderPainted(false);
        btnL2Poner.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL2Poner.setFocusPainted(false);
        btnL2Poner.addActionListener(this::btnL2PonerActionPerformed);
        pnlLinea2.add(btnL2Poner);
        btnL2Poner.setBounds(15, 225, 230, 36);

        btnL2Construir.setBackground(new java.awt.Color(44, 62, 80));
        btnL2Construir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnL2Construir.setForeground(new java.awt.Color(15, 25, 35));
        btnL2Construir.setText("CONSTRUIR");
        btnL2Construir.setBorderPainted(false);
        btnL2Construir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL2Construir.setEnabled(false);
        btnL2Construir.setFocusPainted(false);
        btnL2Construir.addActionListener(this::btnL2ConstruirActionPerformed);
        pnlLinea2.add(btnL2Construir);
        btnL2Construir.setBounds(15, 268, 230, 36);

        btnL2Cancelar.setForeground(new java.awt.Color(231, 76, 60));
        btnL2Cancelar.setText("Cancelar pedido");
        btnL2Cancelar.setBorderPainted(false);
        btnL2Cancelar.setContentAreaFilled(false);
        btnL2Cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnL2Cancelar.setFocusPainted(false);
        btnL2Cancelar.addActionListener(this::btnL2CancelarActionPerformed);
        pnlLinea2.add(btnL2Cancelar);
        btnL2Cancelar.setBounds(15, 308, 230, 24);

        pnlFondo.add(pnlLinea2);
        pnlLinea2.setBounds(720, 95, 260, 340);

        pnlCinta.setBackground(new java.awt.Color(27, 40, 54));
        pnlCinta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(44, 62, 80)));
        pnlCinta.setLayout(null);

        lblCintaTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblCintaTitulo.setForeground(new java.awt.Color(243, 156, 18));
        lblCintaTitulo.setText("CINTA TRANSPORTADORA");
        pnlCinta.add(lblCintaTitulo);
        lblCintaTitulo.setBounds(15, 10, 400, 24);

        lblCintaCapacidad.setForeground(new java.awt.Color(149, 165, 166));
        lblCintaCapacidad.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCintaCapacidad.setText("3 / 3 espacios");
        pnlCinta.add(lblCintaCapacidad);
        lblCintaCapacidad.setBounds(600, 12, 205, 20);

        pnlSlots.setBackground(new java.awt.Color(15, 25, 35));
        pnlSlots.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlSlots.setLayout(new java.awt.GridLayout(1, 0));
        pnlCinta.add(pnlSlots);
        pnlSlots.setBounds(15, 42, 790, 145);

        pnlFondo.add(pnlCinta);
        pnlCinta.setBounds(20, 450, 820, 200);

        pnlBasurero.setBackground(new java.awt.Color(27, 40, 54));
        pnlBasurero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 76, 60)));
        pnlBasurero.setLayout(null);

        lblBasTit.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblBasTit.setForeground(new java.awt.Color(231, 76, 60));
        lblBasTit.setText("BASURERO");
        pnlBasurero.add(lblBasTit);
        lblBasTit.setBounds(15, 10, 200, 24);

        lblBasSelTit.setForeground(new java.awt.Color(149, 165, 166));
        lblBasSelTit.setText("Material seleccionado");
        pnlBasurero.add(lblBasSelTit);
        lblBasSelTit.setBounds(15, 42, 370, 18);

        lblBasMaterial.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblBasMaterial.setForeground(new java.awt.Color(236, 240, 241));
        lblBasMaterial.setText("Ninguno");
        pnlBasurero.add(lblBasMaterial);
        lblBasMaterial.setBounds(15, 60, 370, 28);

        lblBasPerdida.setBackground(new java.awt.Color(255, 255, 255));
        lblBasPerdida.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblBasPerdida.setForeground(new java.awt.Color(231, 76, 60));
        lblBasPerdida.setText("Pérdida si desecha: $0");
        pnlBasurero.add(lblBasPerdida);
        lblBasPerdida.setBounds(15, 90, 370, 20);

        btnDesechar.setBackground(new java.awt.Color(231, 76, 60));
        btnDesechar.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnDesechar.setForeground(new java.awt.Color(255, 255, 255));
        btnDesechar.setText("DESECHAR MATERIAL");
        btnDesechar.setBorderPainted(false);
        btnDesechar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDesechar.setFocusPainted(false);
        btnDesechar.addActionListener(this::btnDesecharActionPerformed);
        pnlBasurero.add(btnDesechar);
        btnDesechar.setBounds(15, 120, 370, 45);

        lblBasTotal.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblBasTotal.setForeground(new java.awt.Color(149, 165, 166));
        lblBasTotal.setText("Materiales desechados: 0");
        pnlBasurero.add(lblBasTotal);
        lblBasTotal.setBounds(15, 174, 370, 18);

        pnlFondo.add(pnlBasurero);
        pnlBasurero.setBounds(860, 450, 400, 200);

        lblMensaje.setBackground(new java.awt.Color(27, 40, 54));
        lblMensaje.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lblMensaje.setForeground(new java.awt.Color(236, 240, 241));
        lblMensaje.setText("   Bienvenido a la Fábrica 1. Asigná un pedido para empezar.");
        lblMensaje.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(243, 156, 18)));
        lblMensaje.setOpaque(true);
        pnlFondo.add(lblMensaje);
        lblMensaje.setBounds(20, 665, 1240, 50);

        getContentPane().add(pnlFondo, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnL3PonerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL3PonerActionPerformed
        ponerMaterial(3);
    }//GEN-LAST:event_btnL3PonerActionPerformed

    private void btnL1PonerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1PonerActionPerformed
        ponerMaterial(1);
    }//GEN-LAST:event_btnL1PonerActionPerformed

    private void btnL2PonerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL2PonerActionPerformed
        ponerMaterial(2);
    }//GEN-LAST:event_btnL2PonerActionPerformed

    private void btnDesecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesecharActionPerformed
        if (posicionSeleccionada == -1) {
            mostrarMensaje("Primero seleccioná un material de la cinta para desecharlo.");
            return;
        }
        String nombre = obtenerMaterialCinta(posicionSeleccionada).getTipo().getAuto();
        double perdida = fabrica.desecharMaterial(posicionSeleccionada);

        materialesDesechados++;
        lblBasTotal.setText("Materiales desechados: " + materialesDesechados);
        mostrarMensaje(nombre + " desechado. Perdiste " + formatearDinero(perdida) + ".");

        limpiarSeleccion();
        refrescarTodo();
    }//GEN-LAST:event_btnDesecharActionPerformed

    private void cmbLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLineaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbLineaActionPerformed

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        int fila = tblPedidos.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Primero seleccioná un pedido de la tabla.");
            return;
        }

        String identificador = (String) tblPedidos.getValueAt(fila, 0);
        int numeroLinea = cmbLinea.getSelectedIndex() + 1;   // índice 0 → Línea 1

        if (fabrica.asignarPedido(identificador, numeroLinea)) {
            mostrarMensaje(identificador + " asignado a la Línea " + numeroLinea
                    + ". Ahora poné los materiales que necesita.");
        } else {
            mostrarMensaje("No se pudo asignar: la Línea " + numeroLinea
                    + " está ocupada o el pedido ya no está disponible.");
        }
        refrescarTodo();
    }//GEN-LAST:event_btnAsignarActionPerformed

    private void btnL1ConstruirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1ConstruirActionPerformed
        construir(1);
    }//GEN-LAST:event_btnL1ConstruirActionPerformed

    private void btnL2ConstruirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL2ConstruirActionPerformed
        construir(2);
    }//GEN-LAST:event_btnL2ConstruirActionPerformed

    private void btnL3ConstruirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL3ConstruirActionPerformed
        construir(3);
    }//GEN-LAST:event_btnL3ConstruirActionPerformed

    private void btnL1CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1CancelarActionPerformed
        cancelar(1);
    }//GEN-LAST:event_btnL1CancelarActionPerformed

    private void btnL2CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL2CancelarActionPerformed
        cancelar(2);
    }//GEN-LAST:event_btnL2CancelarActionPerformed

    private void btnL3CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL3CancelarActionPerformed
        cancelar(3);
    }//GEN-LAST:event_btnL3CancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnDesechar;
    private javax.swing.JButton btnL1Cancelar;
    private javax.swing.JButton btnL1Construir;
    private javax.swing.JButton btnL1Poner;
    private javax.swing.JButton btnL2Cancelar;
    private javax.swing.JButton btnL2Construir;
    private javax.swing.JButton btnL2Poner;
    private javax.swing.JButton btnL3Cancelar;
    private javax.swing.JButton btnL3Construir;
    private javax.swing.JButton btnL3Poner;
    private javax.swing.JComboBox<String> cmbLinea;
    private javax.swing.JLabel lblBasMaterial;
    private javax.swing.JLabel lblBasPerdida;
    private javax.swing.JLabel lblBasSelTit;
    private javax.swing.JLabel lblBasTit;
    private javax.swing.JLabel lblBasTotal;
    private javax.swing.JLabel lblCintaCapacidad;
    private javax.swing.JLabel lblCintaTitulo;
    private javax.swing.JLabel lblDinero;
    private javax.swing.JLabel lblDineroTit;
    private javax.swing.JLabel lblFabrica;
    private javax.swing.JLabel lblHubInfo;
    private javax.swing.JLabel lblHubTitulo;
    private javax.swing.JLabel lblL1Estado;
    private javax.swing.JLabel lblL1Ganancia;
    private javax.swing.JLabel lblL1Mat1;
    private javax.swing.JLabel lblL1Mat2;
    private javax.swing.JLabel lblL1Mat3;
    private javax.swing.JLabel lblL1MatTit;
    private javax.swing.JLabel lblL1Nombre;
    private javax.swing.JLabel lblL1Pedido;
    private javax.swing.JLabel lblL2Estado;
    private javax.swing.JLabel lblL2Ganancia;
    private javax.swing.JLabel lblL2Mat1;
    private javax.swing.JLabel lblL2Mat2;
    private javax.swing.JLabel lblL2Mat3;
    private javax.swing.JLabel lblL2MatTit;
    private javax.swing.JLabel lblL2Nombre;
    private javax.swing.JLabel lblL2Pedido;
    private javax.swing.JLabel lblL3Estado;
    private javax.swing.JLabel lblL3Ganancia;
    private javax.swing.JLabel lblL3Mat1;
    private javax.swing.JLabel lblL3Mat2;
    private javax.swing.JLabel lblL3Mat3;
    private javax.swing.JLabel lblL3MatTit;
    private javax.swing.JLabel lblL3Nombre;
    private javax.swing.JLabel lblL3Pedido;
    private javax.swing.JLabel lblMensaje;
    private javax.swing.JLabel lblObjetivo;
    private javax.swing.JLabel lblObjetivoTit;
    private javax.swing.JLabel lblPerdidas;
    private javax.swing.JLabel lblPerdidasTit;
    private javax.swing.JLabel lblProgresoTit;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JProgressBar pbProgreso;
    private javax.swing.JPanel pnlBasurero;
    private javax.swing.JPanel pnlCinta;
    private javax.swing.JPanel pnlDinero;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlHub;
    private javax.swing.JPanel pnlHud;
    private javax.swing.JPanel pnlL1Sep;
    private javax.swing.JPanel pnlL2Sep;
    private javax.swing.JPanel pnlL3Sep;
    private javax.swing.JPanel pnlLinea1;
    private javax.swing.JPanel pnlLinea2;
    private javax.swing.JPanel pnlLinea39;
    private javax.swing.JPanel pnlObjetivo;
    private javax.swing.JPanel pnlPerdidas;
    private javax.swing.JPanel pnlSlots;
    private javax.swing.JScrollPane scrPedidos;
    private javax.swing.JTable tblPedidos;
    // End of variables declaration//GEN-END:variables
}
