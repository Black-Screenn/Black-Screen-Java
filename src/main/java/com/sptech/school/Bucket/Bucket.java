/*package Bucket;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Bucket {

    private final S3Client s3Client;
    private final String bucketName;

    public Bucket (String bucketName, String region) {

        this.bucketName = bucketName;
        Region region1 = Region.of("us-east-1");
        this.s3Client = S3Client.builder()
                .region(region1)
                .build();
        System.out.println("Conectado a regiao " + region);
    }
    public void baixarArquivoDoBucket(String s3Key, String caminhoLocalDestino) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(this.bucketName)
                .key(s3Key)
                .build();
        s3Client.getObject(getObjectRequest, Paths.get(caminhoLocalDestino));
        System.out.println("Download concluído: " + caminhoLocalDestino);
    }

    public List<String> listarBuckets(){
        System.out.println("Buscando objetos no bucket " + this.bucketName);
        ListObjectsV2Request request =
                ListObjectsV2Request
                        .builder()
                        .bucket(this.bucketName)
                        .build();
        ListObjectsV2Response response  =s3Client.listObjectsV2(request);

        List<String> nomesBuckets = response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
        System.out.println("Listado buckets. Encontrados " + nomesBuckets.size() + " objetos.");
        return nomesBuckets;
    }

    public void fecharConexcao(){
        if(s3Client != null){
            s3Client.close();
        }
    }

    public PutObjectResponse enviarArquivoBucket(String tipoArquivoBucket, String nomeArquivo, String caminhoLocalArquivo){


        String s3Key = tipoArquivoBucket + "/" + nomeArquivo;

        PutObjectRequest objectRequest = PutObjectRequest
                .builder()
                .bucket(this.bucketName)
                .key(s3Key)
                .build();

        System.out.println("Enviando arquivo: " + nomeArquivo + " para S3 Key: " + s3Key);
        PutObjectResponse response = this.s3Client.putObject(
                objectRequest,
                RequestBody.fromFile(Paths.get(caminhoLocalArquivo))
        );

        System.out.println("Upload concluído com sucesso! ETag: " + response.eTag());
        return response;
    }
}
*/