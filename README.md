# 🧩 API de Automatización de Tareas y Programación Reactiva

Esta API ha sido desarrollada para gestionar de forma **automática, escalable y reactiva** la ejecución de tareas en un entorno distribuido. Permite crear, modificar, y sincronizar tareas programadas asociadas a diferentes empresas, controlando la lógica de ejecución en tiempo real a través de **MongoDB Change Streams** y un sistema de **schedulers dinámicos**.

## 🚀 Descripción General

El sistema implementa un **mecanismo de escucha reactiva** sobre la base de datos MongoDB. Cada vez que se detecta un cambio (creación, actualización o eliminación) sobre una tarea (`Task`), el sistema reconfigura automáticamente los `Schedules` activos asociados a esa tarea y a sus empresas.

Se utilizan **Spring Boot y Project Reactor** para el manejo asíncrono de flujos, permitiendo que los cambios se apliquen en tiempo real sin necesidad de reiniciar la aplicación.

---

## 🧠 Características Principales

- **Programación dinámica de tareas**
- **Gestión reactiva de cambios**
- **Cancelación automática de tareas**
- **Reconfiguración inmediata**
- **Arquitectura modular y desacoplada**
- **Compatibilidad multicompañía**
- **Integración con MongoDB Change Streams**

---

## 🧩 Arquitectura y Componentes

| Componente | Descripción |
|-------------|-------------|
| **ReactiveSchedulerListener** | Escucha los eventos del stream de MongoDB y gestiona la creación o cancelación de tareas programadas. |
| **TaskChangeStreamService** | Encapsula la lógica para suscribirse al `ChangeStream` de MongoDB de manera reactiva. |
| **ScheduleRegistry** | Fábrica de instancias de `AbstractSchedule`, encargada de crear y registrar las tareas programadas. |
| **SchedulerConfig** | Contiene las expresiones cron activas y permite su actualización dinámica. |
| **AbstractSchedule** | Clase base que define el comportamiento general de una tarea programada (inicio, cancelación, reprogramación). |

---

## ⚙️ Configuración de MongoDB Reactivo

Para habilitar el monitoreo reactivo de los cambios en la colección `task`, se debe ejecutar el siguiente comando al iniciar MongoDB.  
Esto crea una colección de **oplogs** necesaria para los `Change Streams`.

### 📄 Archivo: `enableMongoChangeStream.bat`

```bat
@echo off
echo === Habilitando Change Stream para MongoDB ===
mongosh --eval "rs.initiate()"
echo Replica Set iniciado correctamente.
pause
```

> 💡 **Nota:** Los `Change Streams` requieren que MongoDB esté corriendo como un **Replica Set**, incluso si solo tienes un nodo local.

---

## 🧰 Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Data MongoDB Reactive**
- **Project Reactor**
- **MongoDB**
- **Lombok**
- **Scheduler personalizado**

---

## 🔄 Flujo de Ejecución

1. Se levanta el **Replica Set de MongoDB**.
2. `TaskChangeStreamService` se suscribe al `Change Stream` de la colección `task`.
3. Al detectar un evento (`insert`, `update`, o `delete`):
   - Se mapea el `Document` a una entidad `Task`.
   - Si la tarea fue eliminada o desactivada → se cancela el schedule.
   - Si la tarea fue creada o modificada → se reprograma automáticamente.
4. `ScheduleRegistry` crea nuevas instancias de `AbstractSchedule` y las registra en memoria.
5. `SchedulerConfig` mantiene actualizadas las expresiones cron en uso.

---

## 🧪 Ejecución del Proyecto

### Requisitos previos

- Java 21+
- Maven 3.9+
- MongoDB 7.x
- Puerto `8080` disponible

### Pasos

```bash
# 1️⃣ Iniciar MongoDB en modo Replica Set
> enableMongoChangeStream.bat

# 2️⃣ Compilar y ejecutar la API
$ mvn clean install
$ mvn spring-boot:run
```

---

## 🌐 Endpoints Principales

| Método | Ruta | Descripción |
|--------|------|--------------|
| `GET` | `/companies` | Lista empresas con paginación y filtros. |
| `POST` | `/tasks` | Crea una nueva tarea programada. |
| `PUT` | `/tasks/{id}` | Actualiza la configuración de una tarea. |
| `DELETE` | `/tasks/{id}` | Elimina una tarea y cancela su programación. |

---

## 🧩 Consideraciones Técnicas

- Comunicación desacoplada mediante **inyección de dependencias**.
- Los schedulers se almacenan en memoria (`activeSchedules`).
- MongoDB Change Stream permite un modelo **event-driven**.
- Los endpoints paginados devuelven un formato estable con `PageResponse<T>`.

---

## 🧪 Próximas Funcionalidades

- Métricas Prometheus.
- Panel administrativo web.
- Cache Redis.
- Pruebas unitarias e integración.

---

## 👨‍💻 Autor

**Sebastian Narvaez Lopera**  
Ingeniero de Software — Codesa  
📧 narvaezsebas8@gmail.com  
🌐 [GitHub: sebastiannarvaez23](https://github.com/sebastiannarvaez23)

---

## 🧾 Licencia

Este proyecto está bajo la licencia **MIT**.
