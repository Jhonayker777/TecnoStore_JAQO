package View;

import Dao.CelularDao;
import Dao.VentaDao;
import Model.Celular;
import Model.CelularVenta;
import Model.Venta;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VistaReportes {

    private final VentaDao ventaDao = new VentaDao();
    private final CelularDao celularDao = new CelularDao();

    private static final String CARPETA = "reportes";
    private static final String ARCHIVO = "reporte_ventas.txt";
    private static final String RUTA = CARPETA + File.separator + ARCHIVO;

    public void generar() {
        List<Venta> ventas = ventaDao.listar();
        List<Celular> celulares = celularDao.listar();

        StringBuilder sb = new StringBuilder();

        sb.append("==================================================");
        sb.append("        REPORTE DE VENTAS - TECNOSTORE             ");
        sb.append("==================================================");

        sb.append(resumenGeneral(ventas));
        sb.append(top3MasVendidos(ventas));
        sb.append(ventasPorMes(ventas));
        sb.append(stockBajo(celulares));
        sb.append(todasLasVentas(ventas));

        sb.append("==================================================");
        sb.append("              FIN DEL REPORTE                    ");
        sb.append("==================================================");

        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            bw.write(sb.toString());
            System.out.println("Reporte generado: " + new File(RUTA).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al escribir reporte: " + e.getMessage());
        }
    }

    private String resumenGeneral(List<Venta> ventas) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN GENERAL ===\n\n");

        double total = ventas.stream().mapToDouble(Venta::getTotal).sum();
        long numVentas = ventas.size();
        double ticket = numVentas > 0 ? total / numVentas : 0;
        long unidades = ventas.stream()
                .flatMap(v -> v.getItems().stream())
                .mapToLong(CelularVenta::getCantidad)
                .sum();

        sb.append(String.format("Número de ventas:    %d%n", numVentas));
        sb.append(String.format("Total facturado:     $%,.2f%n", total));
        sb.append(String.format("Ticket promedio:     $%,.2f%n", ticket));
        sb.append(String.format("Unidades vendidas:   %d%n%n", unidades));

        return sb.toString();
    }

    private String top3MasVendidos(List<Venta> ventas) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TOP 3 MÁS VENDIDOS ===\n\n");

        Map<Celular, Integer> unidades = ventas.stream()
                .flatMap(v -> v.getItems().stream())
                .collect(Collectors.groupingBy(
                        CelularVenta::getCelular,
                        Collectors.summingInt(CelularVenta::getCantidad)
                ));

        List<Map.Entry<Celular, Integer>> top3 = unidades.entrySet().stream()
                .sorted(Map.Entry.<Celular, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());

        if (top3.isEmpty()) {
            sb.append("No hay ventas registradas.\n\n");
        } else {
            int pos = 1;
            for (Map.Entry<Celular, Integer> entry : top3) {
                sb.append(String.format("%d. %s %s → %d unidades%n",
                        pos++,
                        entry.getKey().getMarca().getNombre(),
                        entry.getKey().getModelo(),
                        entry.getValue()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private String ventasPorMes(List<Venta> ventas) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== VENTAS POR MES ===\n\n");

        Map<YearMonth, Double> porMes = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> YearMonth.from(v.getFecha()),
                        Collectors.summingDouble(Venta::getTotal)
                ));

        if (porMes.isEmpty()) {
            sb.append("No hay ventas registradas.\n\n");
        } else {
            porMes.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> sb.append(String.format("%s → $%,.2f%n",
                    e.getKey(), e.getValue())));
            sb.append("\n");
        }

        return sb.toString();
    }

    private String stockBajo(List<Celular> celulares) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CELULARES CON STOCK BAJO ===\n\n");

        List<Celular> bajos = celulares.stream()
                .filter(c -> c.getStock() < c.getStock_minimo())
                .sorted(Comparator.comparingInt(Celular::getStock))
                .collect(Collectors.toList());

        if (bajos.isEmpty()) {
            sb.append("No hay celulares con stock bajo.\n\n");
        } else {
            for (Celular c : bajos) {
                sb.append(String.format("%s %s (stock: %d/%d)%n",
                        c.getMarca().getNombre(),
                        c.getModelo(),
                        c.getStock(),
                        c.getStock_minimo()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private String todasLasVentas(List<Venta> ventas) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TODAS LAS VENTAS ===\n\n");

        if (ventas.isEmpty()) {
            sb.append("No hay ventas registradas.\n");
            return sb.toString();
        }

        for (Venta v : ventas) {
            sb.append(String.format("Venta #%d | %s | Cliente: %s | Total: $%,.2f%n",
                    v.getId(),
                    v.getFecha(),
                    v.getCliente().getNombre(),
                    v.getTotal()));

            for (CelularVenta item : v.getItems()) {
                sb.append(String.format("   - %s x%d @ $%,.2f%n",
                        item.getCelular().getModelo(),
                        item.getCantidad(),
                        item.getPrecioUnitario().getMonto()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
