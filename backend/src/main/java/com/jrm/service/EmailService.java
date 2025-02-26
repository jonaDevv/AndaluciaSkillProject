// package com.jrm.service; // Asegúrate de que este paquete coincida con el tuyo

// import com.jrm.model.Participant; // Asegúrate de que estas importaciones son correctas para tu proyecto
// import com.jrm.model.Prueba;
// import com.jrm.model.User;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

//     private final Logger logger = LoggerFactory.getLogger(EmailService.class);

//     @Autowired
//     private JavaMailSender mailSender;

//     // **NUEVO MÉTODO: Construye el cuerpo del correo dinámicamente**
//     private String construirCuerpoCorreoNuevaEvaluacion(User experto, Participant participante, Prueba prueba) {
//         return "Hola " + experto.getNombre() + ",\n\n" +  // Asumiendo que User tiene getName()
//                "Se te ha asignado una nueva evaluación:\n\n" +
//                "Participante: " + participante.getName() + "\n" + // Asumiendo que Participant tiene getName()
//                "Prueba: " + prueba.getEnunciado() + "\n\n" +     // Asumiendo que Prueba tiene getEnunciado()
//                "Por favor, accede a la plataforma para realizar la evaluación.\n\n" +
//                "Atentamente,\n" +
//                "El equipo de la plataforma"; // Puedes personalizar la firma
//     }

//     // Método sendEmail existente - ahora llama al nuevo método para construir el cuerpo
//     public void sendEmailNuevaEvaluacion(String to, String subject, User experto, Participant participante, Prueba prueba) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(to);
//         message.setSubject(subject);
//         message.setText(construirCuerpoCorreoNuevaEvaluacion(experto, participante, prueba)); // Llamar al método de construcción

//         try {
//             mailSender.send(message);
//             logger.info("Correo electrónico de nueva evaluación enviado exitosamente a: {}", to);
//         } catch (Exception e) {
//             logger.error("Error al enviar correo electrónico de nueva evaluación a: {}", to, e);
//         }
//     }


//     // Método sendEmail genérico (opcional, si quieres mantener la versión anterior para otros usos)
//     public void sendEmail(String to, String subject, String body) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(to);
//         message.setSubject(subject);
//         message.setText(body);

//         try {
//             mailSender.send(message);
//             logger.info("Correo electrónico genérico enviado exitosamente a: {}", to);
//         } catch (Exception e) {
//             logger.error("Error al enviar correo electrónico genérico a: {}", to, e);
//         }
//     }
// }