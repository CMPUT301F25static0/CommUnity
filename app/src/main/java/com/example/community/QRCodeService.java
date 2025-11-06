package com.example.community;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class QRCodeService {
    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;

    QRCodeService() {
        this.imageRepository = new ImageRepository();
        this.eventRepository = new EventRepository();
    }

    /**
     * Generates QR code for an event and uploads it to Storage + Firestore
     * Scenario 2, US 02.01.01
     *
     * @param eventID     The event ID
     * @param generatedBy User ID who generated the QR code
     * @return Task that resolves to Image object with storagePath and download URL
     */
    public Task<Image> generateAndUploadQRCode(String eventID, String generatedBy) {
        try {
            byte[] qrCodeBytes = generateQRCodeBytes(eventID);
            String storagePath = "images/events/" + eventID + "/qrcode.png";

            return imageRepository.upload(qrCodeBytes, storagePath, generatedBy)
                    .onSuccessTask(image ->
                            eventRepository.getByID(eventID)
                                    .onSuccessTask(event -> {
                                        if (event == null) {
                                            throw new IllegalArgumentException("Event not found: " + eventID);
                                        }
                                        event.setQrCodeImageURL(image.getImageURL());
                                        event.setQrCodeImageID(image.getImageID());
                                        return eventRepository.update(event).continueWith(t -> image);
                                    })
                    );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    /**
     * Deletes event QR code from Storage/Firestore and clears the Event document
     * No User story
     *
     * @param eventID The event ID
     * @return Task that completes when both Storage/Firestore and Event are updated
     */
    public Task<Void> deleteEventQRCode(String eventID) {
        return eventRepository.getByID(eventID)
                .onSuccessTask(event -> {
                    if (event == null) {
                        throw new IllegalArgumentException("Event not found: " + eventID);
                    }

                    String qrCodeImageID = event.getQrCodeImageID();

                    Task<Void> deleteImageTask = (qrCodeImageID != null)
                            ? imageRepository.delete(qrCodeImageID)
                            : Tasks.forResult(null);

                    event.setQrCodeImageID(null);
                    event.setQrCodeImageURL(null);

                    return deleteImageTask
                            .continueWithTask(t -> eventRepository.update(event));
                });
    }

    /**
     * Generates QR code image bytes for an event
     *
     * @param eventID The event ID
     * @return PNG image bytes
     * @throws WriterException if QR code generation fails
     */
    private byte[] generateQRCodeBytes(String eventID) throws WriterException {
        String qrContent = "event:" + eventID; // temporary...
        int size = 512; // pixels

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, size, size);

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

}
