package com.telcox.notificationservice.service;

import com.telcox.notificationservice.domain.NotificationLog;
import com.telcox.notificationservice.domain.NotificationTemplate;
import com.telcox.notificationservice.repository.NotificationLogRepository;
import com.telcox.notificationservice.repository.NotificationTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/** FR-28..FR-30: template tabanlı mock bildirim gönderimi (SMS/e-posta/push kanalları). */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationTemplateRepository templateRepository;
    private final NotificationLogRepository logRepository;

    public NotificationService(NotificationTemplateRepository templateRepository, NotificationLogRepository logRepository) {
        this.templateRepository = templateRepository;
        this.logRepository = logRepository;
    }

    @Transactional
    public void send(String templateCode, String recipient, Map<String, String> variables) {
        NotificationTemplate template = templateRepository.findById(templateCode)
                .orElseThrow(() -> new IllegalStateException("Şablon bulunamadı: " + templateCode));

        String content = render(template.getContent(), variables);

        // Mock kanal: gerçek bir SMS/e-posta sağlayıcısına bağlanmaz, sadece loglanır ve kaydedilir.
        log.info("[MOCK-{}] to={} content={}", template.getChannel(), recipient, content);
        logRepository.save(new NotificationLog(templateCode, template.getChannel(), recipient, content));
    }

    private String render(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }
}
