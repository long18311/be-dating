package com.example.datingbe;

import com.example.datingbe.entity.InformationField;
import com.example.datingbe.entity.InformationOption;
import com.example.datingbe.entity.InformationType;
import com.example.datingbe.repository.InformationFieldRepository;
import com.example.datingbe.repository.InformationOptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class DatingBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatingBeApplication.class, args);
    }
    @Bean
    @Transactional
    public CommandLineRunner initData(InformationFieldRepository informationFieldRepository,
                                      InformationOptionRepository informationOptionRepository) {
        Logger logger = LoggerFactory.getLogger(DatingBeApplication.class);
        return args -> {
            // Thêm dữ liệu vào cơ sở dữ liệu sử dụng informationFieldRepository
            if (informationFieldRepository.count() == 0) {
                List<InformationField> informationFields = Arrays.asList(
                        new InformationField(1L, InformationType.basic_information, "Cung hoàng đạo", "cung hoàng đạo của bạn là gì?", false, null),
                        new InformationField(2L, InformationType.basic_information, "Giáo dục", "Trình độ học vấn của bạn là gì?", false, null),
                        new InformationField(3L, InformationType.basic_information, "Gia đình tương lai", "Bạn có muốn có con không?", false, null),
                        new InformationField(4L, InformationType.basic_information, "Kiểu tính cách", "Kiểu tính cách của bạn là gì?", false, null),
                        new InformationField(5L, InformationType.basic_information, "Ngôn ngữ tình yêu", "Khi yêu, bạn mong muốn được nhứng điều gì?", false, null),
                        new InformationField(6L, InformationType.basic_information, "Con số tượng trưng", "Bạn thích con số?", false, null),
                        new InformationField(7L, InformationType.hobby, "Sở thích", "Sở thích của bạn là j", true, null),
                        new InformationField(8L, InformationType.passion, "Thú cưng", "Bạn có muốn nuôi thú cưng không?", true, null),
                        new InformationField(9L, InformationType.passion, "Thói quen rựu bia", "Bạn thường uống rựu bia như thế nào?", true, null),
                        new InformationField(10L, InformationType.passion, "Thói quen hút thuốc", "Bạn có hay hút thuốc không?", true, null),
                        new InformationField(11L, InformationType.passion, "Thể dục thể thao", "Bạn có tập thể dục không?", false, null),
                        new InformationField(12L, InformationType.passion, "Chế độ ăn uống", "Bạn có chế độ ăn uống nào không?", false, null),
                        new InformationField(13L, InformationType.passion, "Hoạt động mạng xã hội", "Mức độ hoạt động của bạn trên mạng xã hội?", false, null),
                        new InformationField(14L, InformationType.passion, "Thói quen giấc ngủ", "Thói quen ngủ của bạn thế nào?", false, null),
                        new InformationField(15L, InformationType.profession, "Nghề nghiệp", "Bạn làm nghề gì?", true, null)

                        );
                informationFieldRepository.saveAll(informationFields);
            }

            // Thêm dữ liệu vào cơ sở dữ liệu sử dụng informationOptionRepository
            if (informationOptionRepository.count() == 0) {
                List<InformationField> informationFields = informationFieldRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
                Map<Long, InformationField> informationFieldMap = informationFields.stream()
                        .collect(Collectors.toMap(InformationField::getId, Function.identity()));

                List<InformationOption> informationOptions = Arrays.asList(
                        new InformationOption(1L, informationFieldMap.get(1L), "Bạch Dương"),
                        new InformationOption(2L, informationFieldMap.get(1L), "Kim ngưu"),
                        new InformationOption(3L, informationFieldMap.get(1L), "Song tử"),
                        new InformationOption(4L, informationFieldMap.get(1L), "Cự giải"),
                        new InformationOption(5L, informationFieldMap.get(1L), "Sư tử"),
                        new InformationOption(6L, informationFieldMap.get(1L), "Xử nữ"),
                        new InformationOption(7L, informationFieldMap.get(1L), "Thiên Bình"),
                        new InformationOption(8L, informationFieldMap.get(1L), "Bọ Cạp"),
                        new InformationOption(9L, informationFieldMap.get(1L), "Nhân mã"),
                        new InformationOption(10L, informationFieldMap.get(1L), "Ma kết"),
                        new InformationOption(11L, informationFieldMap.get(1L), "Bảo bình"),
                        new InformationOption(12L, informationFieldMap.get(1L), "Song ngư"),
                        new InformationOption(13L, informationFieldMap.get(2L), "Trung học cơ sở"),
                        new InformationOption(14L, informationFieldMap.get(2L), "Phổ thông"),
                        new InformationOption(15L, informationFieldMap.get(2L), "Cao dẳng"),
                        new InformationOption(16L, informationFieldMap.get(2L), "Đại học"),
                        new InformationOption(17L, informationFieldMap.get(2L), "Thạc sĩ"),
                        new InformationOption(18L, informationFieldMap.get(2L), "Tiến sĩ"),
                        new InformationOption(19L, informationFieldMap.get(2L), "Cao học"),
                        new InformationOption(20L, informationFieldMap.get(3L), "Đang suy nghĩ"),
                        new InformationOption(21L, informationFieldMap.get(3L), "Có muốn có con"),
                        new InformationOption(22L, informationFieldMap.get(3L), "Không muốn có con"),
                        new InformationOption(23L, informationFieldMap.get(4L), "Hướng nội"),
                        new InformationOption(24L, informationFieldMap.get(4L), "Hướng ngoại"),
                        new InformationOption(25L, informationFieldMap.get(4L), "Trầm tính"),
                        new InformationOption(26L, informationFieldMap.get(4L), "Hòa đồng"),
                        new InformationOption(27L, informationFieldMap.get(4L), "Nhoi nhoi"),
                        new InformationOption(28L, informationFieldMap.get(5L), "Những món quà"),
                        new InformationOption(29L, informationFieldMap.get(5L), "Những nụ hôn"),
                        new InformationOption(30L, informationFieldMap.get(5L), "Những lời nói ngọt ngào"),
                        new InformationOption(31L, informationFieldMap.get(6L), "1"),
                        new InformationOption(32L, informationFieldMap.get(6L), "2"),
                        new InformationOption(33L, informationFieldMap.get(6L), "3"),
                        new InformationOption(34L, informationFieldMap.get(6L), "4"),
                        new InformationOption(35L, informationFieldMap.get(6L), "5"),
                        new InformationOption(36L, informationFieldMap.get(6L), "6"),
                        new InformationOption(37L, informationFieldMap.get(6L), "7"),
                        new InformationOption(38L, informationFieldMap.get(6L), "8"),
                        new InformationOption(39L, informationFieldMap.get(6L), "9"),
                        new InformationOption(40L, informationFieldMap.get(6L), "10"),
                        new InformationOption(41L, informationFieldMap.get(7L), "Ăn đêm"),
                        new InformationOption(42L, informationFieldMap.get(7L), "Ngủ"),
                        new InformationOption(43L, informationFieldMap.get(7L), "Phim"),
                        new InformationOption(44L, informationFieldMap.get(7L), "Thể thao"),
                        new InformationOption(45L, informationFieldMap.get(7L), "Xem TV"),
                        new InformationOption(46L, informationFieldMap.get(7L), "Anime"),
                        new InformationOption(47L, informationFieldMap.get(8L), "Chó"),
                        new InformationOption(48L, informationFieldMap.get(8L), "Mèo"),
                        new InformationOption(49L, informationFieldMap.get(8L), "Bò sát"),
                        new InformationOption(50L, informationFieldMap.get(8L), "Động vật lưỡng cư"),
                        new InformationOption(51L, informationFieldMap.get(8L), "Loài chim"),
                        new InformationOption(52L, informationFieldMap.get(8L), "Cá"),
                        new InformationOption(53L, informationFieldMap.get(8L), "Yêu thích nhưng không nuôi"),
                        new InformationOption(54L, informationFieldMap.get(8L), "Khác"),
                        new InformationOption(55L, informationFieldMap.get(8L), "Rùa"),
                        new InformationOption(56L, informationFieldMap.get(8L), "Thỏ"),
                        new InformationOption(57L, informationFieldMap.get(8L), "Không nuôi thú cưng"),
                        new InformationOption(58L, informationFieldMap.get(8L), "Tất cả các loại thú cưng"),
                        new InformationOption(59L, informationFieldMap.get(8L), "Muốn nuôi thú cưng"),
                        new InformationOption(60L, informationFieldMap.get(8L), "Dị ứng với động vật"),
                        new InformationOption(61L, informationFieldMap.get(9L), "Luông dành cho mình"),
                        new InformationOption(62L, informationFieldMap.get(9L), "Luôn tỉnh táo"),
                        new InformationOption(63L, informationFieldMap.get(9L), "Uống có trách nhiệm"),
                        new InformationOption(64L, informationFieldMap.get(9L), "Chỉ những dịp đặc biệt"),
                        new InformationOption(65L, informationFieldMap.get(9L), "Uống giao lưu cuối năm"),
                        new InformationOption(66L, informationFieldMap.get(9L), "Hầu như mỗi tối"),
                        new InformationOption(67L, informationFieldMap.get(10L), "Hút thuốc với bạn bè"),
                        new InformationOption(68L, informationFieldMap.get(10L), "Hút thuốc khi nhậu"),
                        new InformationOption(69L, informationFieldMap.get(10L), "Không hút thuốc"),
                        new InformationOption(70L, informationFieldMap.get(10L), "Hút thường xuyên"),
                        new InformationOption(71L, informationFieldMap.get(10L), "Đăng bỏ dần"),
                        new InformationOption(72L, informationFieldMap.get(11L), "Hằng ngày"),
                        new InformationOption(73L, informationFieldMap.get(11L), "Thường xuyên"),
                        new InformationOption(74L, informationFieldMap.get(11L), "Thi Thoảng"),
                        new InformationOption(75L, informationFieldMap.get(11L), "Không tập"),
                        new InformationOption(76L, informationFieldMap.get(12L), "Ăn thuần chay"),
                        new InformationOption(77L, informationFieldMap.get(12L), "Ăn chay"),
                        new InformationOption(78L, informationFieldMap.get(12L), "Ăn hải sải và rau củ"),
                        new InformationOption(79L, informationFieldMap.get(12L), "Kosher"),
                        new InformationOption(80L, informationFieldMap.get(12L), "Halai"),
                        new InformationOption(81L, informationFieldMap.get(12L), "Chỉ ăn thịt"),
                        new InformationOption(82L, informationFieldMap.get(12L), "Không ăn kiêng"),
                        new InformationOption(83L, informationFieldMap.get(12L), "Khác"),
                        new InformationOption(84L, informationFieldMap.get(13L), "Inluencer"),
                        new InformationOption(85L, informationFieldMap.get(13L), "Hoạt động tích cực"),
                        new InformationOption(86L, informationFieldMap.get(13L), "Không dùng mạng xã hội"),
                        new InformationOption(87L, informationFieldMap.get(13L), "Lướt dạo"),
                        new InformationOption(88L, informationFieldMap.get(14L), "Dạy sớm"),
                        new InformationOption(89L, informationFieldMap.get(14L), "Cú đêm"),
                        new InformationOption(90L, informationFieldMap.get(14L), "Giờ giấc linh hoạt"),
                        new InformationOption(91L, informationFieldMap.get(15L), "marketing"),
                        new InformationOption(92L, informationFieldMap.get(15L), "IT"),
                        new InformationOption(93L, informationFieldMap.get(15L), "SEO"),
                        new InformationOption(94L, informationFieldMap.get(15L), "Bồi bàn"),
                        new InformationOption(95L, informationFieldMap.get(15L), "Pha chế"),
                        new InformationOption(96L, informationFieldMap.get(15L), "khác")
                );
                informationOptionRepository.saveAll(informationOptions);
            }
        };
    }

}
