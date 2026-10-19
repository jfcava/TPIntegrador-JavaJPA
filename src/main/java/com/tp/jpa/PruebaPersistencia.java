package com.tp.jpa;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;

public class PruebaPersistencia {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            //Instancio y persisto 3 CATEGORIAS
            Categoria catPizzas = Categoria.builder().nombre("Pizzas").descripcion("Pizzas artesanales con masa fresca").build();
            Categoria catHamburguesas = Categoria.builder().nombre("Hamburguesas").descripcion("Hamburguesas gourmet con ingredientes frescos").build();
            Categoria catBebidas   = Categoria.builder().nombre("Bebidas").descripcion("Gaseosas, jugos y bebidas frías").build();

            em.persist(catPizzas);
            em.persist(catHamburguesas);
            em.persist(catBebidas);

            //Instancio y persisto 10 PRODUCTOS
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

            //Obtengo CATEGORIAS y agrego PRODUCTOS
            Categoria catPizzasDb = em.find(Categoria.class, catPizzas.getId());
            catPizzasDb.addProducto(p1);
            catPizzasDb.addProducto(p2);
            catPizzasDb.addProducto(p3);

            Categoria catHamburguesasDb = em.find(Categoria.class, catHamburguesas.getId());
            catHamburguesasDb.addProducto(p4);
            catHamburguesasDb.addProducto(p5);
            catHamburguesasDb.addProducto(p6);

            Categoria catBebidasDb = em.find(Categoria.class, catBebidas.getId());
            catBebidasDb.addProducto(p7);
            catBebidasDb.addProducto(p8);
            catBebidasDb.addProducto(p9);
            catBebidasDb.addProducto(p10);

            //Instancio 3 PEDIDOS con 2 DETALLEPEDIDO cada uno
            Pedido pedido1 = new Pedido(LocalDate.now(), FormaPago.TRANSFERENCIA);
            pedido1.addDetallePedido(2, productos.get(0));
            pedido1.addDetallePedido(1, productos.get(6));

            Pedido pedido2 = new Pedido(LocalDate.now(), FormaPago.TARJETA);
            pedido2.addDetallePedido(1, productos.get(3));
            pedido2.addDetallePedido(1, productos.get(9));

            Pedido pedido3 = new Pedido(LocalDate.now(), FormaPago.EFECTIVO);
            pedido3.addDetallePedido(2, productos.get(4));
            pedido3.addDetallePedido(1, productos.get(7));

            //Instancio 2 USUARIOS y asigno los PEDIDOS
            Usuario usuario1 = Usuario.builder()
                    .nombre("Juan Franco")
                    .apellido("Cavallieri")
                    .mail("juan@utn.edu.ar")
                    .celular("223123456")
                    .contrasenia("qwe123")
                    .rol(Rol.ADMIN)
                    .build();

            usuario1.addPedido(pedido1);
            usuario1.addPedido(pedido2);

            Usuario usuario2 = Usuario.builder()
                    .nombre("Roberto")
                    .apellido("Perez")
                    .mail("roberto@utn.edu.ar")
                    .celular("223987654")
                    .contrasenia("asd123")
                    .rol(Rol.USUARIO)
                    .build();

            usuario2.addPedido(pedido3);

            //Persisto USUARIOS y se persisten los PEDIDOS y los DETALLEPEDIDO por CascadeType.ALL
            em.persist(usuario1);
            em.persist(usuario2);

            tx.commit();
            System.out.println("====== CARGA INICIAL COMPLETA ======");

            //PUNTO 5: Actualizar 2 PRODUCTOS
            tx.begin();

            Producto productoA = em.find(Producto.class, productos.get(0).getId());
            Producto productoB = em.find(Producto.class, productos.get(1).getId());

            if(productoA != null && productoB != null){
                productoA.setPrecio(5000.0);
                productoA.setDescripcion("Pizza clásica con salsa de tomate y muzzarella derretida ACTUALIZADA");

                productoB.setPrecio(5500.0);
                productoB.setDescripcion("Pizza con rodajas de tomate fresco, ajo y albahaca ACTUALIZADA");
            }

            tx.commit();
            System.out.println("====== PRODUCTOS 1 Y 2 ACTUALIZADOS ======");

            //PUNTO 6: Buscar Usuario por ID
            Usuario usuarioBuscado = em.find(Usuario.class, usuario1.getId());
            if(usuarioBuscado != null) {
                System.out.println("=== USUARIO ENCONTRADO: " + usuarioBuscado.getId() + "- " + usuarioBuscado.getNombreCompleto());
            }else {
                System.out.println("=== USUARIO NO ENCONTRADO");
            }

            //PUNTO 7: Buscar Usuario por mail (Consulta JPQL)
            Usuario usuarioBuscadoPorMail = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.mail = :email",
                            Usuario.class
                    ).setParameter("email", "roberto@utn.edu.ar")
                    .getSingleResult();
            if(usuarioBuscadoPorMail != null){
                System.out.println("=== USUARIO ENCONTRADO POR EMAIL: " + usuarioBuscadoPorMail.getNombreCompleto());
            }else {
                System.out.println("=== USUARIO NO ENCONTRADO");
            }

            //PUNTO 8: Borrar 1 PRODUCTO
            tx.begin();
            Producto productoAEliminar = em.find(Producto.class, productos.get(8).getId());
            if(productoAEliminar != null){
                Categoria cat = em.find(Categoria.class, catBebidas.getId());
                if(cat != null){
                    cat.getProductos().remove(productoAEliminar);
                }
                em.remove(productoAEliminar);
            }
            tx.commit();
            System.out.println("=== PRODUCTO ID: " + productoAEliminar.getId() + " eliminado.");


            System.out.println(">>> RUTA EXACTA DEL ARCHIVO: " + new java.io.File("./data/jpa_db").getAbsolutePath());

        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
            JPAUtil.close();
        }
    }
}
