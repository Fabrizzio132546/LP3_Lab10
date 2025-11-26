/*************************************************************************************
ARCHIVO	: RepositorioTXT.java
FECHA	: 25/11/2025
*************************************************************************************/

package Ejercicio1.modelo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTXT {
    private final String archivo = "inventario.txt";

    public void guardar(List<ParAsociado> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (ParAsociado par : lista) {
                Equipo e = par.getEquipo();
                Mantenimiento m = par.getMantenimiento();

                String linea = String.join(",",
                    String.valueOf(e.getId()), e.getNombre(), e.getTipo(), e.getDescripcionTecnica(),
                    String.valueOf(m.getId()), m.getDescripcion(), m.getTecnico(),
                    m.getFecha().toString(), String.valueOf(m.getCosto())
                );
                bw.write(linea);
                bw.newLine();
            }
        }
    }

    public List<ParAsociado> cargar() throws IOException {
        List<ParAsociado> recuperados = new ArrayList<>();
        File f = new File(archivo);
        if (!f.exists()) return recuperados;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                if (p.length < 9) continue;

                Equipo e = new Equipo(Integer.parseInt(p[0]), p[1], p[2], p[3]);
                Mantenimiento m = new Mantenimiento(
                    Integer.parseInt(p[4]), p[5], p[6], LocalDate.parse(p[7]), Double.parseDouble(p[8])
                );
                recuperados.add(new ParAsociado(e, m));
            }
        }
        return recuperados;
    }
    
    public void borrarArchivoFisico() {
        File f = new File(archivo);
        if(f.exists()) f.delete();
    }
}