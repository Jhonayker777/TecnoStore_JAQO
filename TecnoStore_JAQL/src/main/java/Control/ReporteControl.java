package Control;

import Dao.CelularDao;
import Dao.VentaDao;
import Model.Celular;
import Model.CelularVenta;
import Model.Venta;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReporteControl {

    private final VentaDao ventaDao = new VentaDao();
    private final CelularDao celularDao = new CelularDao();

    public void stockBajo() {
        List<Celular> celulares = celularDao.listar();

        List<Celular> bajos = celulares.stream()
                .filter(c -> c.getStock() < c.getStock_minimo())
                .sorted(Comparator.comparingInt(Celular::getStock))
                .collect(Collectors.toList());

        System.out.println("\n=== CELULARES CON STOCK BAJO ===\n");

        if (bajos.isEmpty()) {
            System.out.println("No hay celulares con stock bajo.");
            return;
        }

        System.out.printf("%-5s %-15s %-25s %-8s %-8s%n",
                "ID", "MARCA", "MODELO", "STOCK", "MÍN");
        System.out.println("-".repeat(70));

        for (Celular c : bajos) {
            System.out.printf("%-5d %-15s %-25s %-8d %-8d%n",
                    c.getId(),
                    c.getMarca().getNombre(),
                    c.getModelo(),
                    c.getStock(),
                    c.getStock_minimo());
        }
    }

    public void top3MasVendidos() {
        List<Venta> ventas = ventaDao.listar();

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

        System.out.println("\n=== TOP 3 CELULARES MÁS VENDIDOS ===\n");

        if (top3.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        int pos = 1;
        for (Map.Entry<Celular, Integer> entry : top3) {
            System.out.printf("%d. %s %s → %d unidades%n",
                    pos++,
                    entry.getKey().getMarca().getNombre(),
                    entry.getKey().getModelo(),
                    entry.getValue());
        }
    }

    public void ventasPorMes() {
        List<Venta> ventas = ventaDao.listar();

        Map<YearMonth, Double> porMes = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> YearMonth.from(v.getFecha()),
                        Collectors.summingDouble(Venta::getTotal)
                ));

        System.out.println("\n=== VENTAS TOTALES POR MES ===\n");

        if (porMes.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        System.out.printf("%-10s %-15s%n", "MES", "TOTAL");
        System.out.println("-".repeat(30));

        porMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.printf("%-10s $%,.2f%n",
                e.getKey(), e.getValue()));
    }

    public void todasLasVentas() {
        List<Venta> ventas = ventaDao.listar();

        System.out.println("\n=== TODAS LAS VENTAS ===\n");

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        for (Venta v : ventas) {
            System.out.printf("Venta #%d | %s | Cliente: %s | Total: $%,.2f%n",
                    v.getId(),
                    v.getFecha(),
                    v.getCliente().getNombre(),
                    v.getTotal());

            for (CelularVenta item : v.getItems()) {
                System.out.printf("   - %s x%d @ $%,.2f%n",
                        item.getCelular().getModelo(),
                        item.getCantidad(),
                        item.getPrecioUnitario().getMonto());
            }
            System.out.println();
        }
    }

    public void resumenGeneral() {
        List<Venta> ventas = ventaDao.listar();

        double total = ventas.stream().mapToDouble(Venta::getTotal).sum();
        long numVentas = ventas.size();
        double ticket = numVentas > 0 ? total / numVentas : 0;
        long unidades = ventas.stream()
                .flatMap(v -> v.getItems().stream())
                .mapToLong(CelularVenta::getCantidad)
                .sum();

        System.out.println("\n=== RESUMEN GENERAL ===\n");
        System.out.printf("Número de ventas:    %d%n", numVentas);
        System.out.printf("Total facturado:     $%,.2f%n", total);
        System.out.printf("Ticket promedio:     $%,.2f%n", ticket);
        System.out.printf("Unidades vendidas:   %d%n", unidades);
    }

    public void todos() {
        resumenGeneral();
        stockBajo();
        top3MasVendidos();
        ventasPorMes();
        todasLasVentas();
    }
}
