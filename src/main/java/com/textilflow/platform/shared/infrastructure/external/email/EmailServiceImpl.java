package com.textilflow.platform.shared.infrastructure.external.email;

import com.textilflow.platform.shared.application.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender,
                            @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String userName, String userRole) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("¡Bienvenido a TextilFlow! 🧵");

            String roleText = "businessman".equals(userRole) ? "empresario textil" : "proveedor textil";
            String roleIcon = "businessman".equals(userRole) ? "🏢" : "🏭";

            String htmlContent = buildWelcomeEmailTemplate(userName, roleText, roleIcon, toEmail);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending welcome email: " + e.getMessage(), e);
        }
    }
    @Override
    public void sendPasswordResetEmail(String toEmail, String userName, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Restablece tu contraseña - TextilFlow 🔐");

            String htmlContent = buildPasswordResetEmailTemplate(userName, resetLink, toEmail);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending password reset email: " + e.getMessage(), e);
        }
    }
    private String buildWelcomeEmailTemplate(String userName, String roleText, String roleIcon, String toEmail) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Bienvenido a TextilFlow Platform</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }

                    body {
                        font-family: 'Arial', sans-serif;
                        line-height: 1.6;
                        color: #333;
                        background-color: #f5f5f5;
                    }

                    .email-container {
                        max-width: 600px;
                        margin: 20px auto;
                        background-color: #ffffff;
                        border-radius: 12px;
                        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                    }

                    .header {
                        background: linear-gradient(135deg, #866C52 0%%, #6e573f 100%%);
                        color: white;
                        text-align: center;
                        padding: 40px 20px;
                    }

                    .header h1 {
                        font-size: 32px;
                        margin-bottom: 10px;
                        font-weight: bold;
                    }

                    .header .subtitle {
                        font-size: 18px;
                        opacity: 0.9;
                    }

                    .textile-icon {
                        font-size: 48px;
                        margin-bottom: 20px;
                        display: block;
                    }

                    .content {
                        padding: 40px 30px;
                    }

                    .welcome-section {
                        text-align: center;
                        margin-bottom: 30px;
                    }

                    .welcome-section h2 {
                        color: #866C52;
                        font-size: 28px;
                        margin-bottom: 15px;
                    }

                    .user-info {
                        background-color: rgba(134, 108, 82, 0.1);
                        border-left: 4px solid #866C52;
                        padding: 20px;
                        margin: 25px 0;
                        border-radius: 8px;
                    }

                    .role-badge {
                        display: inline-block;
                        background-color: #866C52;
                        color: white;
                        padding: 8px 16px;
                        border-radius: 20px;
                        font-size: 14px;
                        font-weight: bold;
                        margin: 10px 0;
                    }

                    .features {
                        margin: 30px 0;
                    }

                    .feature-item {
                        display: flex;
                        align-items: center;
                        margin-bottom: 15px;
                        padding: 10px;
                        background-color: #f9f9f9;
                        border-radius: 8px;
                    }

                    .feature-icon {
                        font-size: 24px;
                        margin-right: 15px;
                        color: #866C52;
                    }

                    .cta-button {
                        display: inline-block;
                        background: linear-gradient(135deg, #866C52 0%%, #6e573f 100%%);
                        color: white;
                        text-decoration: none;
                        padding: 15px 30px;
                        border-radius: 8px;
                        font-weight: bold;
                        margin: 20px 0;
                        transition: transform 0.2s ease;
                    }

                    .cta-button:hover {
                        transform: translateY(-2px);
                    }

                    .footer {
                        background-color: #f8f9fa;
                        padding: 30px;
                        text-align: center;
                        border-top: 1px solid #eee;
                    }

                    .footer p {
                        color: #666;
                        font-size: 14px;
                        margin-bottom: 10px;
                    }

                    .social-links {
                        margin-top: 20px;
                    }

                    .social-links a {
                        display: inline-block;
                        margin: 0 10px;
                        color: #866C52;
                        text-decoration: none;
                        font-weight: bold;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        <span class="textile-icon">🧵</span>
                        <h1>TextilFlow Platform</h1>
                        <div class="subtitle">Conectando el mundo textil</div>
                    </div>

                    <div class="content">
                        <div class="welcome-section">
                            <h2>¡Bienvenido, %s! %s</h2>
                            <p style="font-size: 18px; color: #666; margin-bottom: 20px;">
                                Nos emociona tenerte como parte de nuestra comunidad textil
                            </p>
                        </div>

                        <div class="user-info">
                            <p><strong>Tu cuenta ha sido creada exitosamente</strong></p>
                            <div class="role-badge">%s %s</div>
                            <p style="margin-top: 15px; color: #666;">
                                Ahora tienes acceso completo a todas las funcionalidades de nuestra plataforma.
                            </p>
                        </div>

                        <div class="features">
                            <h3 style="color: #866C52; margin-bottom: 20px;">¿Qué puedes hacer ahora?</h3>

                            <div class="feature-item">
                                <span class="feature-icon">🤝</span>
                                <div>
                                    <strong>Conectar con socios</strong><br>
                                    <span style="color: #666;">Encuentra empresarios y proveedores ideales para tu negocio</span>
                                </div>
                            </div>

                            <div class="feature-item">
                                <span class="feature-icon">📊</span>
                                <div>
                                    <strong>Gestionar tu perfil</strong><br>
                                    <span style="color: #666;">Completa tu información y destaca en el mercado</span>
                                </div>
                            </div>

                            <div class="feature-item">
                                <span class="feature-icon">💬</span>
                                <div>
                                    <strong>Comunicación directa</strong><br>
                                    <span style="color: #666;">Establece relaciones comerciales sólidas y duraderas</span>
                                </div>
                            </div>

                            <div class="feature-item">
                                <span class="feature-icon">🎯</span>
                                <div>
                                    <strong>Oportunidades de negocio</strong><br>
                                    <span style="color: #666;">Accede a una red exclusiva del sector textil</span>
                                </div>
                            </div>
                        </div>

                        <div style="text-align: center; margin: 30px 0;">
                            <a href="#" class="cta-button">Comenzar ahora 🚀</a>
                        </div>

                        <div style="background-color: rgba(134, 108, 82, 0.1); padding: 20px; border-radius: 8px; text-align: center;">
                            <p style="color: #866C52; font-weight: bold; margin-bottom: 10px;">
                                💡 Consejo: Completa tu perfil al 100%%
                            </p>
                            <p style="color: #666; font-size: 14px;">
                                Los perfiles completos tienen 5x más oportunidades de conexión
                            </p>
                        </div>
                    </div>

                    <div class="footer">
                        <p><strong>¡Gracias por unirte a TextilFlow Platform!</strong></p>
                        <p>Si tienes alguna pregunta, no dudes en contactarnos.</p>

                        <div class="social-links">
                            <a href="#">📧 Soporte</a>
                            <a href="https://textilflow.web.app/login">📱 Log in</a>
                            <a href="https://textilflow-curso-open-source.github.io/Landing-Page-TextilFlow/">🌐 Sitio Web</a>
                        </div>

                        <p style="margin-top: 20px; font-size: 12px; color: #999;">
                            © 2024 TextilFlow Platform. Todos los derechos reservados.<br>
                            Este correo fue enviado a %s
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """, userName, roleIcon, roleText, roleIcon, toEmail);
    }
    private String buildPasswordResetEmailTemplate(String userName, String resetLink, String toEmail) {
        return String.format("""
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Restablece tu contraseña - TextilFlow</title>
            <style>
                /* Mismo CSS que el welcome email */
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; background-color: #f5f5f5; }
                .email-container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); overflow: hidden; }
                .header { background: linear-gradient(135deg, #866C52 0%%, #6e573f 100%%); color: white; text-align: center; padding: 40px 20px; }
                .header h1 { font-size: 32px; margin-bottom: 10px; font-weight: bold; }
                .textile-icon { font-size: 48px; margin-bottom: 20px; display: block; }
                .content { padding: 40px 30px; }
                .reset-section { text-align: center; margin-bottom: 30px; }
                .reset-section h2 { color: #866C52; font-size: 28px; margin-bottom: 15px; }
                .security-info { background-color: rgba(255, 193, 7, 0.1); border-left: 4px solid #ffc107; padding: 20px; margin: 25px 0; border-radius: 8px; }
                .cta-button { display: inline-block; background: linear-gradient(135deg, #866C52 0%%, #6e573f 100%%); color: white; text-decoration: none; padding: 15px 30px; border-radius: 8px; font-weight: bold; margin: 20px 0; }
                .footer { background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #eee; }
                .footer p { color: #666; font-size: 14px; margin-bottom: 10px; }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="header">
                    <span class="textile-icon">🔐</span>
                    <h1>TextilFlow Platform</h1>
                </div>
                <div class="content">
                    <div class="reset-section">
                        <h2>¡Hola, %s!</h2>
                        <p style="font-size: 18px; color: #666; margin-bottom: 20px;">
                            Recibimos una solicitud para restablecer tu contraseña
                        </p>
                    </div>
                    <div class="security-info">
                        <p><strong>⏰ Este enlace expira en 60 minutos</strong></p>
                        <p style="margin-top: 10px; color: #666;">
                            Si no solicitaste este cambio, puedes ignorar este email.
                        </p>
                    </div>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" class="cta-button">Restablecer Contraseña 🔑</a>
                    </div>
                    <p style="color: #666; font-size: 14px; text-align: center;">
                        Si el botón no funciona, copia y pega este enlace:<br>
                        <a href="%s" style="color: #866C52; word-break: break-all;">%s</a>
                    </p>
                </div>
                <div class="footer">
                    <p><strong>TextilFlow Platform - Seguridad</strong></p>
                    <p>Este correo fue enviado a %s</p>
                </div>
            </div>
        </body>
        </html>
        """, userName, resetLink, resetLink, resetLink, toEmail);
    }
}