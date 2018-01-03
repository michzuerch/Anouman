package ch.internettechnik.anouman.presentation.ui;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import server.droporchoose.UploadComponent;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageField extends CustomField<Byte[]> {
    private Byte[] imageData;
    private UploadComponent upload = new UploadComponent();
    private Image image = new Image();

    private String filename;
    private String mimeType;

    @Override
    public Byte[] getEmptyValue() {
        try {
            return ArrayUtils.toObject(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("/images/EmptyImage.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //@todo Validator für Bilder (NPE)
//    @Override
//    public Validator<byte[]> getDefaultValidator() {
//        return null;
//    }

    @Override
    protected void doSetValue(Byte[] value) {
        this.imageData = value;
        update();
    }

    @Override
    public Byte[] getValue() {
        return imageData;
    }

    @Override
    protected Component initContent() {
        upload.setReceivedCallback(this::uploadReceived);
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSpacing(false);
        image.setSource(new StreamResource(new ImageSource(ArrayUtils.toPrimitive(getValue())), "EmptyImage.jpg"));
        image.setHeight(400, Unit.PIXELS);
        layout.addComponents(image, upload);
        return layout;
    }

    private void uploadReceived(String s, Path path) {
        try {
            Byte[] uploadData = ArrayUtils.toObject(Files.readAllBytes(Paths.get(path.toUri())));
            setFilename(s);
            setMimeType(Files.probeContentType(path));
            if (getMimeType().equals("image/jpeg") || (getMimeType().equals("image/png"))) {
                doSetValue(uploadData);
            } else {
                Notification.show("Nur Bilder als JPG oder PNG erlaubt (MIME-Type)", Notification.Type.ERROR_MESSAGE);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void update() {
        try {
            if (ImageIO.read(new ByteArrayInputStream(ArrayUtils.toPrimitive(getValue()))) != null) {
                // Update the image according to
                // https://vaadin.com/book/vaadin7/-/page/components.embedded.html
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String filename = df.format(new Date()) + "-image.jpg";
                StreamResource resource = new StreamResource(new ImageSource(ArrayUtils.toPrimitive(getValue())), filename);
                resource.setCacheTime(0);
                image.setSource(resource);
            } else {
                image.setSource(null);
                setValue(null);
            }
            image.markAsDirty();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public class ImageSource implements StreamResource.StreamSource {

        private final byte[] buffer;

        public ImageSource(byte[] image) {
            this.buffer = image;
        }

        @Override
        public InputStream getStream() {
            return new ByteArrayInputStream(buffer);
        }
    }


}
