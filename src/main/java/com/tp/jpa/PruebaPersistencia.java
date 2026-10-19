package com.tp.jpa;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class PruebaPersistencia {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Categoria catPizzas = Categoria.builder().nombre("Pizzas").descripcion("Pizzas artesanales con masa fresca").build();
            Categoria catHamburguesas = Categoria.builder().nombre("Hamburguesas").descripcion("Hamburguesas gourmet con ingredientes frescos").build();
            Categoria catBebidas   = Categoria.builder().nombre("Bebidas").descripcion("Gaseosas, jugos y bebidas frías").build();

            em.persist(catPizzas);
            em.persist(catHamburguesas);
            em.persist(catBebidas);

            Producto p1 = Producto.builder().nombre("Pizza Muzzarella").precio(4500.0).descripcion("Pizza clásica con salsa de tomate y muzzarella derretida").stock(20).disponible(true).build();
            Producto p2 = Producto.builder().nombre("Pizza Napolitana").precio(5200.0).descripcion("Pizza con rodajas de tomate fresco, ajo y albahaca").stock(15).disponible(true).build();
            Producto p3 = Producto.builder().nombre("Pizza Especial 4 Quesos").precio(6800.0).descripcion("Muzzarella, provolone, roquefort y parmesano").stock(10).disponible(true).build();
            Producto p4 = Producto.builder().nombre("Hamburguesa Clásica").precio(3800.0).descripcion("Medallón de carne, lechuga, tomate, cebolla y mayo").stock(30).disponible(true).build();
            Producto p5 = Producto.builder().nombre("Hamburguesa BBQ Bacon").precio(5100.0).descripcion("Doble medallón, bacon crocante y salsa barbacoa ahumada").stock(25).disponible(true).build();
            Producto p6 = Producto.builder().nombre("Hamburguesa Veggie").precio(4200.0).descripcion("Medallón de lentejas y garbanzo, cheddar vegano y rúcula").stock(0).disponible(false).build();
            Producto p7 = Producto.builder().nombre("Coca-Cola 500ml").precio(1200.0).descripcion("Gaseosa Coca-Cola fría, botella personal").stock(100).disponible(true).build();
            Producto p8 = Producto.builder().nombre("Jugo de Naranja Natural").precio(1800.0).descripcion("Jugo exprimido en el momento, vaso 400ml").stock(40).disponible(true).build();
            Producto p9 = Producto.builder().nombre("Agua Mineral 500ml").precio(800.0).descripcion("Agua mineral sin gas, botella personal").stock(150).disponible(true).build();
            Producto p10 = Producto.builder().nombre("Seven Up").precio(1200.0).descripcion("Gaseosa Seven Up fría, botella personal").stock(50).disponible(true).build();

            List<Producto> productos = List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10);

            for(Producto producto : productos){
                em.persist(producto);
            }

            Categoria catPizzasDb = em.find(Categoria.class, catPizzas.getId());
            catPizzas.addProducto(p1);
            catPizzas.addProducto(p2);
            catPizzas.addProducto(p3);

            Categoria catHamburguesasDb = em.find(Categoria.class, catHamburguesas.getId());
            catHamburguesas.addProducto(p4);
            catHamburguesas.addProducto(p5);
            catHamburguesas.addProducto(p6);

            Categoria catBebidasDb = em.find(Categoria.class, catBebidas.getId());
            catBebidas.addProducto(p7);
            catBebidas.addProducto(p8);
            catBebidas.addProducto(p9);
            catBebidas.addProducto(p10);


        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
            JPAUtil.close();
        }
    }
}
