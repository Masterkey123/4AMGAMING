package com.example.mobilefixhw;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilefixhw.R; // Ensure your R file is in this package

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText ampInput;
    private EditText chgAmpInput;
    private Button checkBtn;
    private TextView resultText;
    private TextView digitalVoltageDisplay;
    private TextView digitalAmpDisplay;
    private TextView faultDisplayBox;
    private LinearLayout errorDetailsLayout;
    private RelativeLayout cpuTypeSelectionOverlay;
    private LinearLayout cpuTypeSelectionLayout;
    private ImageButton cpuTypeDropdownButton;
    private LinearLayout chgAmpSection;
    private RelativeLayout ampInputContainer;

    private List<String> cpuTypesList;
    private String selectedCpuType = "MTK"; // Default to MTK
    private DecimalFormat df;

    // Data structures for CFR values and behaviors
    private Map<String, List<CFRValue>> cpuCFRValues;
    private Map<String, List<Behavior>> cpuBehaviors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        df = new DecimalFormat("0.0000");

        cpuTypesList = new ArrayList<>();
        cpuTypesList.add("MTK");
        cpuTypesList.add("Qualcomm");
        cpuTypesList.add("Huawei");
        cpuTypesList.add("Samsung");
        cpuTypesList.add("LG");
        cpuTypesList.add("Oppo");
        cpuTypesList.add("Vivo");
        cpuTypesList.add("Xiaomi");
        cpuTypesList.add("Apple");
        cpuTypesList.add("Chg Amp"); // Add "Chg Amp" option

        initData(); // Initialize your data here

        ampInput = findViewById(R.id.amp_input);
        checkBtn = findViewById(R.id.check_btn);
        resultText = findViewById(R.id.result_text);
        digitalVoltageDisplay = findViewById(R.id.digital_voltage_display);
        digitalAmpDisplay = findViewById(R.id.digital_amp_display);
        faultDisplayBox = findViewById(R.id.fault_display_box);
        errorDetailsLayout = findViewById(R.id.error_details_layout);
        cpuTypeSelectionOverlay = findViewById(R.id.cpu_type_selection_overlay);
        cpuTypeSelectionLayout = findViewById(R.id.cpu_type_selection_layout);
        cpuTypeDropdownButton = findViewById(R.id.cpu_type_dropdown_button);
        chgAmpInput = findViewById(R.id.chg_amp_input);
        chgAmpSection = findViewById(R.id.chg_amp_section);
        ampInputContainer = findViewById(R.id.amp_input_container);

        updateAmpInputHint();

        checkBtn.setOnClickListener(v -> calculateAmp());

        cpuTypeDropdownButton.setOnClickListener(v -> showCpuTypeDropdown());

        cpuTypeSelectionOverlay.setOnClickListener(v -> hideCpuTypeDropdown());

        // Initial setup for the digital voltage display
        // Assuming you want this to change later
        digitalVoltageDisplay.setText("4.3 V"); // Set initial text

        // Set initial fault and amp display values
        digitalAmpDisplay.setText("0.0000 A");
        faultDisplayBox.setText("No Fault");
        faultDisplayBox.setTextColor(0xFF00FF00); // Green color

    }

    private void initData() {
        cpuCFRValues = new HashMap<>();
        cpuBehaviors = new HashMap<>();

        // MTK
        List<CFRValue> mtkCFR = new ArrayList<>();
        mtkCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        mtkCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        mtkCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        mtkCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        mtkCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        mtkCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        mtkCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        mtkCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        mtkCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        mtkCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        mtkCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        mtkCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        mtkCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        mtkCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        mtkCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        mtkCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("MTK", mtkCFR);

        List<Behavior> mtkBehaviors = new ArrayList<>();
        mtkBehaviors.add(new Behavior("CFR1", "CPU VCORE LDO 1V2 POWER OK", "အောက်က Power I C ပျက်", "CPU IC", "CPU IC or Power IC"));
        mtkBehaviors.add(new Behavior("CFR2", "CPU LDO VOLTAGE DROP", "CPU LDO Voltage ကျ", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        mtkBehaviors.add(new Behavior("CFR3", "CPU CORE POWER FAULT", "CPU Core Power ချွတ်ယွင်း", "CPU Power Line Test", "CPU IC or Power IC"));
        mtkBehaviors.add(new Behavior("CFR4", "CPU THERMAL SHUTDOWN", "CPU အပူချိန်လွန်ပြီး ပိတ်သွား", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        mtkBehaviors.add(new Behavior("CFR5", "CPU INTERNAL CLOCK FAULT", "CPU Clock အလုပ်မလုပ်", "CPU Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        mtkBehaviors.add(new Behavior("CFR6", "CPU BUS INTERFACE ERROR", "CPU Bus အင်တာဖေ့စ် ချွတ်ယွင်း", "CPU Bus Line Test", "CPU IC or Motherboard"));
        mtkBehaviors.add(new Behavior("CFR7", "CPU RESET FAILURE", "CPU Reset မအောင်မြင်", "CPU Reset Circuit Test", "CPU IC or Reset IC"));
        mtkBehaviors.add(new Behavior("CFR8", "CPU BOOTROM ERROR", "CPU Bootrom အမှား", "CPU Bootrom Check", "CPU IC or EMMC/NAND"));
        mtkBehaviors.add(new Behavior("CFR9", "CPU EMI INTERFERENCE", "CPU EMI အနှောင့်အယှက်", "EMI Shield Check", "Motherboard or EMI Shield"));
        mtkBehaviors.add(new Behavior("CFR10", "CPU POWER RAIL OVP", "CPU Power Rail Over Voltage", "Power Rail Voltage Check", "Power IC or PMIC"));
        mtkBehaviors.add(new Behavior("CFR11", "CPU MEMORY CONTROLLER ERROR", "CPU Memory Controller ချွတ်ယွင်း", "Memory Interface Test", "CPU IC or RAM"));
        mtkBehaviors.add(new Behavior("CFR12", "CPU GFX POWER FAULT", "CPU GFX Power ချွတ်ယွင်း", "GFX Power Line Test", "CPU IC or GPU IC"));
        mtkBehaviors.add(new Behavior("CFR13", "CPU SECURITY VIOLATION", "CPU လုံခြုံရေးချိုးဖောက်မှု", "Security Feature Check", "CPU IC or Software Issue"));
        mtkBehaviors.add(new Behavior("CFR14", "CPU DAC/ADC ERROR", "CPU DAC/ADC ချွတ်ယွင်း", "DAC/ADC Test", "CPU IC or Codec IC"));
        mtkBehaviors.add(new Behavior("CFR15", "CPU JTAG/DEBUG PORT ERROR", "CPU JTAG/Debug Port ချွတ်ယွင်း", "JTAG Port Check", "CPU IC or Motherboard"));
        cpuBehaviors.put("MTK", mtkBehaviors);

        // Qualcomm
        List<CFRValue> qualcommCFR = new ArrayList<>();
        qualcommCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        qualcommCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        qualcommCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        qualcommCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        qualcommCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        qualcommCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        qualcommCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        qualcommCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        qualcommCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        qualcommCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        qualcommCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        qualcommCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        qualcommCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        qualcommCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        qualcommCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        qualcommCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("Qualcomm", qualcommCFR);

        List<Behavior> qualcommBehaviors = new ArrayList<>();
        qualcommBehaviors.add(new Behavior("CFR1", "PMIC Output Fault", "PMIC အထွက်ချွတ်ယွင်း", "PMIC Output Voltage Test", "PMIC or Power IC"));
        qualcommBehaviors.add(new Behavior("CFR2", "CPU LDO Undervoltage", "CPU LDO ဗို့အားကျ", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        qualcommBehaviors.add(new Behavior("CFR3", "CPU Core Voltage Instability", "CPU Core Voltage မတည်ငြိမ်", "CPU Core Voltage Test", "CPU IC or Power IC"));
        qualcommBehaviors.add(new Behavior("CFR4", "CPU Overheating Protection", "CPU အပူလွန်ကာကွယ်မှု", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        qualcommBehaviors.add(new Behavior("CFR5", "Clock Generator Error", "Clock Generator အမှား", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        qualcommBehaviors.add(new Behavior("CFR6", "Bus Communication Failure", "Bus ဆက်သွယ်မှု ချွတ်ယွင်း", "Bus Line Test", "CPU IC or Motherboard"));
        qualcommBehaviors.add(new Behavior("CFR7", "System Reset Loop", "System Reset ဖြစ်နေ", "Reset Circuit Test", "CPU IC or Reset IC"));
        qualcommBehaviors.add(new Behavior("CFR8", "Bootloader Corruption", "Bootloader ပျက်စီး", "Bootloader Re-flash", "CPU IC or EMMC/NAND"));
        qualcommBehaviors.add(new Behavior("CFR9", "RF Interference on CPU Lines", "CPU Line တွေမှာ RF အနှောင့်အယှက်", "RF Shield Check", "Motherboard or RF Shield"));
        qualcommBehaviors.add(new Behavior("CFR10", "Power Rail Overcurrent", "Power Rail Over Current", "Power Rail Current Check", "Power IC or PMIC"));
        qualcommBehaviors.add(new Behavior("CFR11", "DDR Memory Error", "DDR Memory အမှား", "Memory Interface Test", "CPU IC or RAM"));
        qualcommBehaviors.add(new Behavior("CFR12", "GPU Power Domain Issue", "GPU Power Domain ပြဿနာ", "GPU Power Line Test", "CPU IC or GPU IC"));
        qualcommBehaviors.add(new Behavior("CFR13", "Secure Boot Failure", "Secure Boot မအောင်မြင်", "Secure Boot Check", "CPU IC or Software Issue"));
        qualcommBehaviors.add(new Behavior("CFR14", "Audio Codec Communication Error", "Audio Codec ဆက်သွယ်မှု အမှား", "Audio Codec Test", "CPU IC or Codec IC"));
        qualcommBehaviors.add(new Behavior("CFR15", "Debug Interface Lockup", "Debug Interface Lock ဖြစ်", "Debug Port Check", "CPU IC or Motherboard"));
        cpuBehaviors.put("Qualcomm", qualcommBehaviors);

        // Huawei
        List<CFRValue> huaweiCFR = new ArrayList<>();
        huaweiCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        huaweiCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        huaweiCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        huaweiCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        huaweiCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        huaweiCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        huaweiCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        huaweiCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        huaweiCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        huaweiCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        huaweiCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        huaweiCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        huaweiCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        huaweiCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        huaweiCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        huaweiCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("Huawei", huaweiCFR);

        List<Behavior> huaweiBehaviors = new ArrayList<>();
        huaweiBehaviors.add(new Behavior("CFR1", "HiSilicon PMU Fault", "HiSilicon PMU ချွတ်ယွင်း", "PMU Voltage Test", "HiSilicon IC or PMU"));
        huaweiBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Fluctuation", "CPU LDO ဗို့အားအတက်အကျ", "CPU LDO Voltage Measurement", "HiSilicon IC or PMU"));
        huaweiBehaviors.add(new Behavior("CFR3", "CPU Core Power Rail Sag", "CPU Core Power Rail ကျဆင်း", "CPU Core Voltage Test", "HiSilicon IC or PMU"));
        huaweiBehaviors.add(new Behavior("CFR4", "Thermal Throttling Engaged", "Thermal Throttling အလုပ်လုပ်", "CPU Thermal Sensor Test", "HiSilicon IC or Thermal Circuit"));
        huaweiBehaviors.add(new Behavior("CFR5", "System Clock Instability", "System Clock မတည်ငြိမ်", "Clock Circuit Test", "HiSilicon IC or Crystal Oscillator"));
        huaweiBehaviors.add(new Behavior("CFR6", "Inter-Processor Communication Error", "Processor များအကြား ဆက်သွယ်မှု အမှား", "IPC Bus Test", "HiSilicon IC or Motherboard"));
        huaweiBehaviors.add(new Behavior("CFR7", "Power-on Reset (POR) Loop", "Power-on Reset (POR) ဖြစ်နေ", "Reset Circuit Test", "HiSilicon IC or Reset IC"));
        huaweiBehaviors.add(new Behavior("CFR8", "eMMC/UFS Initialization Failure", "eMMC/UFS စတင်ခြင်း မအောင်မြင်", "eMMC/UFS Check", "HiSilicon IC or Storage IC"));
        huaweiBehaviors.add(new Behavior("CFR9", "Antenna or RF Interference", "Antenna သို့မဟုတ် RF အနှောင့်အယှက်", "Antenna/RF Shield Check", "Motherboard or RF Shield"));
        huaweiBehaviors.add(new Behavior("CFR10", "Battery Management Unit (BMU) Error", "Battery Management Unit (BMU) အမှား", "Battery Voltage/Current Check", "BMU IC or Battery"));
        huaweiBehaviors.add(new Behavior("CFR11", "RAM Training Error", "RAM Training အမှား", "Memory Interface Test", "HiSilicon IC or RAM"));
        huaweiBehaviors.add(new Behavior("CFR12", "NPU Power Domain Issue", "NPU Power Domain ပြဿနာ", "NPU Power Line Test", "HiSilicon IC or NPU"));
        huaweiBehaviors.add(new Behavior("CFR13", "Security Boot Chain Broken", "Security Boot Chain ပျက်စီး", "Security Feature Check", "HiSilicon IC or Software Issue"));
        huaweiBehaviors.add(new Behavior("CFR14", "Audio DSP Communication Error", "Audio DSP ဆက်သွယ်မှု အမှား", "Audio DSP Test", "HiSilicon IC or Codec IC"));
        huaweiBehaviors.add(new Behavior("CFR15", "Fastboot Mode Lock", "Fastboot Mode Lock ဖြစ်", "Fastboot Port Check", "HiSilicon IC or Motherboard"));
        cpuBehaviors.put("Huawei", huaweiBehaviors);

        // Samsung
        List<CFRValue> samsungCFR = new ArrayList<>();
        samsungCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        samsungCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        samsungCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        samsungCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        samsungCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        samsungCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        samsungCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        samsungCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        samsungCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        samsungCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        samsungCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        samsungCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        samsungCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        samsungCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        samsungCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        samsungCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("Samsung", samsungCFR);

        List<Behavior> samsungBehaviors = new ArrayList<>();
        samsungBehaviors.add(new Behavior("CFR1", "Exynos PMIC Output Error", "Exynos PMIC အထွက်ချွတ်ယွင်း", "PMIC Output Voltage Test", "Exynos IC or PMIC"));
        samsungBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Drift", "CPU LDO ဗို့အားပြောင်းလဲ", "CPU LDO Voltage Measurement", "Exynos IC or PMIC"));
        samsungBehaviors.add(new Behavior("CFR3", "CPU Core Power Rail Instability", "CPU Core Power Rail မတည်ငြိမ်", "CPU Core Voltage Test", "Exynos IC or PMIC"));
        samsungBehaviors.add(new Behavior("CFR4", "Thermal Sensor Malfunction", "Thermal Sensor အလုပ်မလုပ်", "CPU Thermal Sensor Test", "Exynos IC or Thermal Circuit"));
        samsungBehaviors.add(new Behavior("CFR5", "Clock Source Corruption", "Clock Source ပျက်စီး", "Clock Circuit Test", "Exynos IC or Crystal Oscillator"));
        samsungBehaviors.add(new Behavior("CFR6", "Interconnect Bus Error", "Interconnect Bus အမှား", "Bus Line Test", "Exynos IC or Motherboard"));
        samsungBehaviors.add(new Behavior("CFR7", "Watchdog Timer Reset", "Watchdog Timer Reset ဖြစ်", "Watchdog Circuit Test", "Exynos IC or Reset IC"));
        samsungBehaviors.add(new Behavior("CFR8", "UFS/eMMC Boot Failure", "UFS/eMMC Boot မအောင်မြင်", "UFS/eMMC Check", "Exynos IC or Storage IC"));
        samsungBehaviors.add(new Behavior("CFR9", "EMI Shielding Compromised", "EMI Shielding ပျက်စီး", "EMI Shield Check", "Motherboard or EMI Shield"));
        samsungBehaviors.add(new Behavior("CFR10", "Fuel Gauge IC Error", "Fuel Gauge IC အမှား", "Battery Fuel Gauge Check", "Fuel Gauge IC or Battery"));
        samsungBehaviors.add(new Behavior("CFR11", "DRAM Interface Error", "DRAM Interface အမှား", "Memory Interface Test", "Exynos IC or RAM"));
        samsungBehaviors.add(new Behavior("CFR12", "ISP Power Domain Issue", "ISP Power Domain ပြဿနာ", "ISP Power Line Test", "Exynos IC or ISP"));
        samsungBehaviors.add(new Behavior("CFR13", "Knox Security Breach", "Knox Security ချိုးဖောက်မှု", "Knox Feature Check", "Exynos IC or Software Issue"));
        samsungBehaviors.add(new Behavior("CFR14", "Audio Chip Communication Error", "Audio Chip ဆက်သွယ်မှု အမှား", "Audio Chip Test", "Exynos IC or Codec IC"));
        samsungBehaviors.add(new Behavior("CFR15", "Download Mode Loop", "Download Mode Loop ဖြစ်", "Download Port Check", "Exynos IC or Motherboard"));
        cpuBehaviors.put("Samsung", samsungBehaviors);

        // LG
        List<CFRValue> lgCFR = new ArrayList<>();
        lgCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        lgCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        lgCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        lgCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        lgCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        lgCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        lgCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        lgCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        lgCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        lgCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        lgCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        lgCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        lgCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        lgCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        lgCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        lgCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("LG", lgCFR);

        List<Behavior> lgBehaviors = new ArrayList<>();
        lgBehaviors.add(new Behavior("CFR1", "Power Management IC (PMIC) Fault", "PMIC ချွတ်ယွင်း", "PMIC Output Voltage Test", "PMIC or Power IC"));
        lgBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Instability", "CPU LDO ဗို့အားမတည်ငြိမ်", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        lgBehaviors.add(new Behavior("CFR3", "CPU Core Power Delivery Issue", "CPU Core Power ပေးပို့မှု ပြဿနာ", "CPU Core Voltage Test", "CPU IC or Power IC"));
        lgBehaviors.add(new Behavior("CFR4", "Overheat Protection Activated", "အပူလွန် ကာကွယ်မှု အလုပ်လုပ်", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        lgBehaviors.add(new Behavior("CFR5", "Clock Signal Interruption", "Clock Signal ပြတ်တောက်", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        lgBehaviors.add(new Behavior("CFR6", "Data Bus Communication Error", "Data Bus ဆက်သွယ်မှု အမှား", "Data Bus Line Test", "CPU IC or Motherboard"));
        lgBehaviors.add(new Behavior("CFR7", "System Reset Malfunction", "System Reset အလုပ်မလုပ်", "Reset Circuit Test", "CPU IC or Reset IC"));
        lgBehaviors.add(new Behavior("CFR8", "Boot Mode Entry Failure", "Boot Mode ဝင်ရောက်ခြင်း မအောင်မြင်", "Boot Mode Check", "CPU IC or EMMC/NAND"));
        lgBehaviors.add(new Behavior("CFR9", "Electromagnetic Interference (EMI)", "လျှပ်စစ်သံလိုက် နှောင့်ယှက်မှု", "EMI Shield Check", "Motherboard or EMI Shield"));
        lgBehaviors.add(new Behavior("CFR10", "Battery Charging IC Error", "ဘက်ထရီအားသွင်း IC အမှား", "Charging IC Test", "Charging IC or Battery"));
        lgBehaviors.add(new Behavior("CFR11", "RAM Access Fault", "RAM Access ချွတ်ယွင်း", "Memory Interface Test", "CPU IC or RAM"));
        lgBehaviors.add(new Behavior("CFR12", "Graphics Processing Unit (GPU) Power Issue", "GPU Power ပြဿနာ", "GPU Power Line Test", "CPU IC or GPU IC"));
        lgBehaviors.add(new Behavior("CFR13", "Firmware Integrity Check Failure", "Firmware Integrity Check မအောင်မြင်", "Firmware Check", "CPU IC or Software Issue"));
        lgBehaviors.add(new Behavior("CFR14", "Audio Codec IC Malfunction", "Audio Codec IC အလုပ်မလုပ်", "Audio Codec Test", "CPU IC or Codec IC"));
        lgBehaviors.add(new Behavior("CFR15", "USB Debugging Port Error", "USB Debugging Port အမှား", "USB Port Check", "CPU IC or Motherboard"));
        cpuBehaviors.put("LG", lgBehaviors);

        // Oppo
        List<CFRValue> oppoCFR = new ArrayList<>();
        oppoCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        oppoCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        oppoCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        oppoCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        oppoCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        oppoCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        oppoCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        oppoCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        oppoCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        oppoCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        oppoCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        oppoCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        oppoCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        oppoCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        oppoCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        oppoCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("Oppo", oppoCFR);

        List<Behavior> oppoBehaviors = new ArrayList<>();
        oppoBehaviors.add(new Behavior("CFR1", "OPPO Power IC Failure", "OPPO Power IC ချွတ်ယွင်း", "Power IC Output Test", "Power IC"));
        oppoBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Drop", "CPU LDO Voltage ကျ", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        oppoBehaviors.add(new Behavior("CFR3", "CPU Core Power Fluctuation", "CPU Core Power မတည်ငြိမ်", "CPU Core Voltage Test", "CPU IC or Power IC"));
        oppoBehaviors.add(new Behavior("CFR4", "Thermal Protection Triggered", "Thermal Protection အလုပ်လုပ်", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        oppoBehaviors.add(new Behavior("CFR5", "Clock Source Error", "Clock Source အမှား", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        oppoBehaviors.add(new Behavior("CFR6", "Data Bus Interruption", "Data Bus ပြတ်တောက်", "Data Bus Line Test", "CPU IC or Motherboard"));
        oppoBehaviors.add(new Behavior("CFR7", "System Reset Loop", "System Reset ဖြစ်နေ", "Reset Circuit Test", "CPU IC or Reset IC"));
        oppoBehaviors.add(new Behavior("CFR8", "Boot Partition Corruption", "Boot Partition ပျက်စီး", "Boot Partition Check", "CPU IC or EMMC/NAND"));
        oppoBehaviors.add(new Behavior("CFR9", "RF Signal Interference", "RF Signal အနှောင့်အယှက်", "RF Shield Check", "Motherboard or RF Shield"));
        oppoBehaviors.add(new Behavior("CFR10", "VOOC/SuperVOOC Charging Failure", "VOOC/SuperVOOC အားသွင်း မအောင်မြင်", "Charging IC Test", "Charging IC or Battery"));
        oppoBehaviors.add(new Behavior("CFR11", "RAM Data Integrity Error", "RAM Data Integrity အမှား", "Memory Interface Test", "CPU IC or RAM"));
        oppoBehaviors.add(new Behavior("CFR12", "GPU Performance Issue", "GPU စွမ်းဆောင်ရည် ပြဿနာ", "GPU Power Line Test", "CPU IC or GPU IC"));
        oppoBehaviors.add(new Behavior("CFR13", "ColorOS Security Error", "ColorOS လုံခြုံရေး အမှား", "Security Feature Check", "CPU IC or Software Issue"));
        oppoBehaviors.add(new Behavior("CFR14", "Audio Processing Unit (APU) Error", "Audio Processing Unit (APU) အမှား", "APU Test", "CPU IC or Codec IC"));
        oppoBehaviors.add(new Behavior("CFR15", "Recovery Mode Loop", "Recovery Mode Loop ဖြစ်", "Recovery Port Check", "CPU IC or Motherboard"));
        cpuBehaviors.put("Oppo", oppoBehaviors);

        // Vivo
        List<CFRValue> vivoCFR = new ArrayList<>();
        vivoCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        vivoCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        vivoCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        vivoCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        vivoCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        vivoCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        vivoCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        vivoCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        vivoCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        vivoCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        vivoCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        vivoCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        vivoCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        vivoCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        vivoCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        vivoCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("Vivo", vivoCFR);

        List<Behavior> vivoBehaviors = new ArrayList<>();
        vivoBehaviors.add(new Behavior("CFR1", "Vivo Power Management IC (PMIC) Fault", "Vivo PMIC ချွတ်ယွင်း", "PMIC Output Test", "PMIC"));
        vivoBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Fluctuation", "CPU LDO Voltage မတည်ငြိမ်", "CPU LDO Voltage Measurement", "CPU IC or PMIC"));
        vivoBehaviors.add(new Behavior("CFR3", "CPU Core Power Delivery Error", "CPU Core Power ပေးပို့မှု အမှား", "CPU Core Voltage Test", "CPU IC or PMIC"));
        vivoBehaviors.add(new Behavior("CFR4", "Thermal System Shutdown", "Thermal System ပိတ်သွား", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        vivoBehaviors.add(new Behavior("CFR5", "Clock Generation Failure", "Clock Generation မအောင်မြင်", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        vivoBehaviors.add(new Behavior("CFR6", "Interconnect Bus Error", "Interconnect Bus အမှား", "Bus Line Test", "CPU IC or Motherboard"));
        vivoBehaviors.add(new Behavior("CFR7", "Software Reset Loop", "Software Reset ဖြစ်နေ", "Reset Circuit Test", "CPU IC or Software Issue"));
        vivoBehaviors.add(new Behavior("CFR8", "Bootloader Read/Write Error", "Bootloader ဖတ်/ရေး အမှား", "Bootloader Check", "CPU IC or EMMC/NAND"));
        vivoBehaviors.add(new Behavior("CFR9", "Wireless Interference", "Wireless အနှောင့်အယှက်", "Wireless Shield Check", "Motherboard or Wireless Module"));
        vivoBehaviors.add(new Behavior("CFR10", "FlashCharge/SuperFlashCharge Failure", "FlashCharge/SuperFlashCharge မအောင်မြင်", "Charging IC Test", "Charging IC or Battery"));
        vivoBehaviors.add(new Behavior("CFR11", "RAM ECC Error", "RAM ECC အမှား", "Memory Interface Test", "CPU IC or RAM"));
        vivoBehaviors.add(new Behavior("CFR12", "DSP Power Domain Issue", "DSP Power Domain ပြဿနာ", "DSP Power Line Test", "CPU IC or DSP"));
        vivoBehaviors.add(new Behavior("CFR13", "Funtouch OS Security Breach", "Funtouch OS လုံခြုံရေး ချိုးဖောက်မှု", "Security Feature Check", "CPU IC or Software Issue"));
        vivoBehaviors.add(new Behavior("CFR14", "Audio Codec Interruption", "Audio Codec ပြတ်တောက်", "Audio Codec Test", "CPU IC or Codec IC"));
        vivoBehaviors.add(new Behavior("CFR15", "Service Mode Lock", "Service Mode Lock ဖြစ်", "Service Port Check", "CPU IC or Motherboard"));
        cpuBehaviors.put("Vivo", vivoBehaviors);

        // Xiaomi
        List<CFRValue> xiaomiCFR = new ArrayList<>();
        xiaomiCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        xiaomiCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        xiaomiCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        xiaomiCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        xiaomiCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        xiaomiCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        xiaomiCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        xiaomiCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        xiaomiCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        xiaomiCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        xiaomiCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        xiaomiCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        xiaomiCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        xiaomiCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        xiaomiCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        xiaomiCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("Xiaomi", xiaomiCFR);

        List<Behavior> xiaomiBehaviors = new ArrayList<>();
        xiaomiBehaviors.add(new Behavior("CFR1", "Xiaomi Power Management IC Fault", "Xiaomi Power IC ချွတ်ယွင်း", "Power IC Output Test", "Power IC"));
        xiaomiBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Drop", "CPU LDO Voltage ကျ", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        xiaomiBehaviors.add(new Behavior("CFR3", "CPU Core Power Irregularity", "CPU Core Power ပုံမမှန်", "CPU Core Voltage Test", "CPU IC or Power IC"));
        xiaomiBehaviors.add(new Behavior("CFR4", "Thermal Shutdown Activated", "Thermal Shutdown အလုပ်လုပ်", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        xiaomiBehaviors.add(new Behavior("CFR5", "Clock Signal Glitch", "Clock Signal ပြဿနာ", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        xiaomiBehaviors.add(new Behavior("CFR6", "System Bus Error", "System Bus အမှား", "Bus Line Test", "CPU IC or Motherboard"));
        xiaomiBehaviors.add(new Behavior("CFR7", "Hardware Reset Loop", "Hardware Reset ဖြစ်နေ", "Reset Circuit Test", "CPU IC or Reset IC"));
        xiaomiBehaviors.add(new Behavior("CFR8", "MIUI Boot Failure", "MIUI Boot မအောင်မြင်", "Bootloader Check", "CPU IC or EMMC/NAND"));
        xiaomiBehaviors.add(new Behavior("CFR9", "Network Interference", "Network အနှောင့်အယှက်", "Network Shield Check", "Motherboard or Network Module"));
        xiaomiBehaviors.add(new Behavior("CFR10", "Mi Charge/HyperCharge Failure", "Mi Charge/HyperCharge မအောင်မြင်", "Charging IC Test", "Charging IC or Battery"));
        xiaomiBehaviors.add(new Behavior("CFR11", "Memory Controller Error", "Memory Controller အမှား", "Memory Interface Test", "CPU IC or RAM"));
        xiaomiBehaviors.add(new Behavior("CFR12", "Image Signal Processor (ISP) Power Issue", "ISP Power ပြဿနာ", "ISP Power Line Test", "CPU IC or ISP"));
        xiaomiBehaviors.add(new Behavior("CFR13", "MIUI Security Feature Disabled", "MIUI လုံခြုံရေး feature ပိတ်", "Security Feature Check", "CPU IC or Software Issue"));
        xiaomiBehaviors.add(new Behavior("CFR14", "Audio Codec Communication Fault", "Audio Codec ဆက်သွယ်မှု ချွတ်ယွင်း", "Audio Codec Test", "CPU IC or Codec IC"));
        xiaomiBehaviors.add(new Behavior("CFR15", "EDL Mode Lock", "EDL Mode Lock ဖြစ်", "EDL Port Check", "CPU IC or Motherboard"));
        cpuBehaviors.put("Xiaomi", xiaomiBehaviors);

        // Apple
        List<CFRValue> appleCFR = new ArrayList<>();
        appleCFR.add(new CFRValue(0.0001, 0.0002, "Normal"));
        appleCFR.add(new CFRValue(0.0003, 0.0005, "CFR1"));
        appleCFR.add(new CFRValue(0.0006, 0.0008, "CFR2"));
        appleCFR.add(new CFRValue(0.0009, 0.0012, "CFR3"));
        appleCFR.add(new CFRValue(0.0013, 0.0015, "CFR4"));
        appleCFR.add(new CFRValue(0.0016, 0.0018, "CFR5"));
        appleCFR.add(new CFRValue(0.0019, 0.0022, "CFR6"));
        appleCFR.add(new CFRValue(0.0023, 0.0025, "CFR7"));
        appleCFR.add(new CFRValue(0.0026, 0.0028, "CFR8"));
        appleCFR.add(new CFRValue(0.0029, 0.0032, "CFR9"));
        appleCFR.add(new CFRValue(0.0033, 0.0035, "CFR10"));
        appleCFR.add(new CFRValue(0.0036, 0.0038, "CFR11"));
        appleCFR.add(new CFRValue(0.0039, 0.0042, "CFR12"));
        appleCFR.add(new CFRValue(0.0043, 0.0045, "CFR13"));
        appleCFR.add(new CFRValue(0.0046, 0.0048, "CFR14"));
        appleCFR.add(new CFRValue(0.0049, 0.0050, "CFR15"));
        cpuCFRValues.put("Apple", appleCFR);

        List<Behavior> appleBehaviors = new ArrayList<>();
        appleBehaviors.add(new Behavior("CFR1", "Apple PMIC Output Failure", "Apple PMIC အထွက်ချွတ်ယွင်း", "PMIC Output Test", "PMIC"));
        appleBehaviors.add(new Behavior("CFR2", "CPU Voltage Regulator Module (VRM) Issue", "CPU VRM ပြဿနာ", "CPU VRM Voltage Measurement", "CPU IC or VRM"));
        appleBehaviors.add(new Behavior("CFR3", "CPU Core Power Rail Sag", "CPU Core Power Rail ကျဆင်း", "CPU Core Voltage Test", "CPU IC or PMIC"));
        appleBehaviors.add(new Behavior("CFR4", "Thermal Management System Fault", "Thermal Management System ချွတ်ယွင်း", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        appleBehaviors.add(new Behavior("CFR5", "System Clock Synchronization Error", "System Clock Synchronization အမှား", "Clock Circuit Test", "CPU IC or Clock Generator"));
        appleBehaviors.add(new Behavior("CFR6", "Inter-Processor Communication (IPC) Failure", "IPC ချွတ်ယွင်း", "IPC Bus Test", "CPU IC or Motherboard"));
        appleBehaviors.add(new Behavior("CFR7", "Hardware Reset Loop", "Hardware Reset ဖြစ်နေ", "Reset Circuit Test", "CPU IC or Reset IC"));
        appleBehaviors.add(new Behavior("CFR8", "Secure Enclave Processor (SEP) Error", "SEP အမှား", "SEP Check", "CPU IC or SEP Module"));
        appleBehaviors.add(new Behavior("CFR9", "RF/Cellular Interference", "RF/Cellular အနှောင့်အယှက်", "RF Shield Check", "Motherboard or RF Module"));
        appleBehaviors.add(new Behavior("CFR10", "Charging IC/Tristar Issue", "Charging IC/Tristar ပြဿနာ", "Charging IC Test", "Charging IC or Battery"));
        appleBehaviors.add(new Behavior("CFR11", "RAM/NAND Interface Error", "RAM/NAND Interface အမှား", "Memory Interface Test", "CPU IC or Memory IC"));
        appleBehaviors.add(new Behavior("CFR12", "GPU Power Draw Anomaly", "GPU Power ဆွဲအား ပုံမမှန်", "GPU Power Line Test", "CPU IC or GPU IC"));
        appleBehaviors.add(new Behavior("CFR13", "Face ID/Touch ID Security Error", "Face ID/Touch ID လုံခြုံရေး အမှား", "Security Feature Check", "CPU IC or Biometric Module"));
        appleBehaviors.add(new Behavior("CFR14", "Audio IC/Codec Communication Failure", "Audio IC/Codec ဆက်သွယ်မှု မအောင်မြင်", "Audio IC Test", "CPU IC or Audio IC"));
        appleBehaviors.add(new Behavior("CFR15", "DFU Mode Lock", "DFU Mode Lock ဖြစ်", "DFU Port Check", "CPU IC or Motherboard"));
        cpuBehaviors.put("Apple", appleBehaviors);


        // Chg Amp
        List<CFRValue> chgAmpCFR = new ArrayList<>();
        chgAmpCFR.add(new CFRValue(0.0001, 0.0002, "Normal (Charging)"));
        chgAmpCFR.add(new CFRValue(0.0003, 0.0005, "ChgCFR1"));
        chgAmpCFR.add(new CFRValue(0.0006, 0.0008, "ChgCFR2"));
        chgAmpCFR.add(new CFRValue(0.0009, 0.0012, "ChgCFR3"));
        chgAmpCFR.add(new CFRValue(0.0013, 0.0015, "ChgCFR4"));
        chgAmpCFR.add(new CFRValue(0.0016, 0.0018, "ChgCFR5"));
        chgAmpCFR.add(new CFRValue(0.0019, 0.0022, "ChgCFR6"));
        chgAmpCFR.add(new CFRValue(0.0023, 0.0025, "ChgCFR7"));
        chgAmpCFR.add(new CFRValue(0.0026, 0.0028, "ChgCFR8"));
        chgAmpCFR.add(new CFRValue(0.0029, 0.0032, "ChgCFR9"));
        chgAmpCFR.add(new CFRValue(0.0033, 0.0035, "ChgCFR10"));
        chgAmpCFR.add(new CFRValue(0.0036, 0.0038, "ChgCFR11"));
        chgAmpCFR.add(new CFRValue(0.0039, 0.0042, "ChgCFR12"));
        chgAmpCFR.add(new CFRValue(0.0043, 0.0045, "ChgCFR13"));
        chgAmpCFR.add(new CFRValue(0.0046, 0.0048, "ChgCFR14"));
        chgAmpCFR.add(new CFRValue(0.0049, 0.0050, "ChgCFR15"));
        cpuCFRValues.put("Chg Amp", chgAmpCFR);

        List<Behavior> chgAmpBehaviors = new ArrayList<>();
        chgAmpBehaviors.add(new Behavior("ChgCFR1", "Battery Temperature High", "ဘက်ထရီ အပူချိန်လွန်", "ဘက်ထရီ/NTC အာရုံခံစနစ် စစ်ဆေးပါ", "ဘက်ထရီ သို့မဟုတ် NTC အပူချိန်ထိန်းချုပ်မှု"));
        chgAmpBehaviors.add(new Behavior("ChgCFR2", "Charging IC Fault", "အားသွင်း IC ပျက်", "အားသွင်း IC ဗို့အား၊ အထွက်၊ ground တွေ စစ်ဆေးပါ", "Charging IC"));
        chgAmpBehaviors.add(new Behavior("ChgCFR3", "USB Data Line Fault", "USB Data Line ပျက်", "USB data line (D+, D-) တွေ စစ်ဆေးပါ", "USB Port or Data Line Circuit"));
        chgAmpBehaviors.add(new Behavior("ChgCFR4", "Battery Connector Issue", "ဘက်ထရီ connector ပြဿနာ", "ဘက်ထရီ connector နဲ့ pin တွေ စစ်ဆေးပါ", "Battery Connector"));
        chgAmpBehaviors.add(new Behavior("ChgCFR5", "Power Supply Low", "Power Supply အားနည်း", "Adapter/charger ဗို့အားနဲ့ current စစ်ဆေးပါ", "Charger/Power Adapter"));
        chgAmpBehaviors.add(new Behavior("ChgCFR6", "Overvoltage Protection Active", "ဗို့အားလွန် ကာကွယ်မှု အလုပ်လုပ်", "ဗို့အားလွန် ကာကွယ်မှု (OVP) IC ကို စစ်ဆေးပါ", "OVP IC or PMIC"));
        chgAmpBehaviors.add(new Behavior("ChgCFR7", "Charging Port Damage", "အားသွင်းပေါက် ပျက်စီး", "အားသွင်းပေါက် (USB port) ပျက်စီးမှု စစ်ဆေးပါ", "Charging Port"));
        chgAmpBehaviors.add(new Behavior("ChgCFR8", "Software Charging Error", "ဆော့ဖ်ဝဲလ် အားသွင်းမှု အမှား", "Firmware/Software ကို Update ဒါမှမဟုတ် Reset လုပ်ပါ", "Software Issue"));
        chgAmpBehaviors.add(new Behavior("ChgCFR9", "Charge Pump Fault", "Charge Pump ပျက်", "Charge Pump IC ကို စစ်ဆေးပါ", "Charge Pump IC"));
        chgAmpBehaviors.add(new Behavior("ChgCFR10", "Thermal Pad Issue", "Thermal Pad ပြဿနာ", "Thermal pad တွေ မှန်ကန်စွာ ထိတွေ့နေမှု စစ်ဆေးပါ", "Thermal Pad or Heatsink"));
        chgAmpBehaviors.add(new Behavior("ChgCFR11", "Auxiliary Charging Path Fault", "အရန် အားသွင်းလမ်းကြောင်း ပျက်", "အရန်အားသွင်းလမ်းကြောင်းကို စစ်ဆေးပါ", "Auxiliary Charging Circuit"));
        chgAmpBehaviors.add(new Behavior("ChgCFR12", "Charger Not Detected", "အားသွင်းကိရိယာ ရှာမတွေ့", "Charger နဲ့ Cable ကို စစ်ဆေးပါ", "Charger or USB Cable"));
        chgAmpBehaviors.add(new Behavior("ChgCFR13", "Charging Communication Error", "အားသွင်း ဆက်သွယ်မှု အမှား", "Communication lines (e.g., CC, DPM) ကို စစ်ဆေးပါ", "Charging IC or Communication Lines"));
        chgAmpBehaviors.add(new Behavior("ChgCFR14", "High Resistance in Charging Path", "အားသွင်းလမ်းကြောင်းမှာ ခံနိုင်ရည်မြင့်မား", "Charging path မှာရှိတဲ့ Resistor တွေ၊ Cable တွေ စစ်ဆေးပါ", "Resistors or Cable"));
        chgAmpBehaviors.add(new Behavior("ChgCFR15", "Battery ID / NTC Fault", "ဘက်ထရီ ID / NTC ပျက်", "ဘက်ထရီ ID pin နဲ့ NTC pin တွေ စစ်ဆေးပါ", "Battery or NTC Circuit"));
        cpuBehaviors.put("Chg Amp", chgAmpBehaviors);
    }

    private void updateAmpInputHint() {
        if ("Chg Amp".equals(selectedCpuType)) {
            ampInput.setHint("Amp ကို Chg Amp ထဲကယူပါ");
            chgAmpSection.setVisibility(View.VISIBLE);
            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(dpToPx(8));
            background.setColor(0xFF333333);
            background.setStroke(dpToPx(1), 0xFFE0C240); // Orange border
            chgAmpInput.setBackground(background);
        } else {
            ampInput.setHint("AMP ရေးပါ");
            chgAmpSection.setVisibility(View.GONE);
            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(dpToPx(8));
            background.setColor(0xFF333333);
            background.setStroke(dpToPx(1), 0xFF007BFF); // Blue border
            ampInput.setBackground(background);
        }
    }

    private void calculateAmp() {
        String ampInputStr;
        if ("Chg Amp".equals(selectedCpuType)) {
            ampInputStr = chgAmpInput.getText().toString();
        } else {
            ampInputStr = ampInput.getText().toString();
        }

        if (ampInputStr.isEmpty()) {
            Toast.makeText(this, "AMP တန်ဖိုး ထည့်ပါ", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double measuredAmp = Double.parseDouble(ampInputStr);
            digitalAmpDisplay.setText(df.format(measuredAmp) + " A");

            CFRValue cfrValue = getCFRValue(measuredAmp, selectedCpuType);
            String cfrName = (cfrValue != null) ? cfrValue.getName() : "Unknown CFR";
            resultText.setText("CFR: " + cfrName);

            updateFaultDisplay(cfrName);
            displayErrorDetails(cfrName);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "မှန်ကန်သော AMP တန်ဖိုး ထည့်ပါ", Toast.LENGTH_SHORT).show();
        }
    }

    private CFRValue getCFRValue(double measuredAmp, String cpuType) {
        List<CFRValue> cfrList = cpuCFRValues.get(cpuType);
        if (cfrList != null) {
            for (CFRValue cfr : cfrList) {
                if (cfr.inRange(measuredAmp)) {
                    return cfr;
                }
            }
        }
        return null;
    }

    private void updateFaultDisplay(String cfrName) {
        if ("Normal".equals(cfrName) || "Normal (Charging)".equals(cfrName)) {
            faultDisplayBox.setText("No Fault");
            faultDisplayBox.setTextColor(0xFF00FF00); // Green
        } else {
            faultDisplayBox.setText("FAULT!");
            faultDisplayBox.setTextColor(0xFFFF0000); // Red
        }
    }

    private void displayErrorDetails(String cfrName) {
        errorDetailsLayout.removeAllViews();
        List<Behavior> behaviors = cpuBehaviors.get(selectedCpuType);

        if (behaviors != null) {
            for (Behavior behavior : behaviors) {
                if (behavior.getCfrCode().equals(cfrName)) {
                    TextView behaviorText = new TextView(this);
                    behaviorText.setText("Fault Code: " + behavior.getCfrCode() + "\n" +
										 "English: " + behavior.getEnglishDescription() + "\n" +
										 "Myanmar: " + behavior.getMyanmarDescription() + "\n" +
										 "Check Point: " + behavior.getCheckPoint() + "\n" +
										 "Solution: " + behavior.getSolution());
                    behaviorText.setTextColor(0xFFFFFFFF);
                    behaviorText.setTextSize(16);
                    behaviorText.setPadding(0, dpToPx(5), 0, dpToPx(5));
                    errorDetailsLayout.addView(behaviorText);
                    return;
                }
            }
        }

        // If no specific behavior found for the CFR name
        if (!"Normal".equals(cfrName) && !"Normal (Charging)".equals(cfrName)) {
            TextView noDetailsText = new TextView(this);
            noDetailsText.setText("No specific details for " + cfrName + " in " + selectedCpuType + ".");
            noDetailsText.setTextColor(0xFFCCCCCC);
            noDetailsText.setTextSize(16);
            errorDetailsLayout.addView(noDetailsText);
        }
    }

    private void showCpuTypeDropdown() {
        cpuTypeSelectionOverlay.setVisibility(View.VISIBLE);
        cpuTypeSelectionLayout.removeAllViews();

        for (String cpuType : cpuTypesList) {
            TextView tv = new TextView(this);
            tv.setText(cpuType);
            tv.setTextColor(0xFFFFFFFF);
            tv.setTextSize(18);
            tv.setPadding(dpToPx(15), dpToPx(10), dpToPx(15), dpToPx(10));
            tv.setGravity(Gravity.CENTER_VERTICAL);

            if (cpuType.equals(selectedCpuType)) {
                tv.setBackgroundColor(0xFF0056B3); // Highlight selected
            } else {
                tv.setBackgroundColor(0x00000000); // Transparent
            }

            tv.setOnClickListener(v -> {
                selectedCpuType = cpuType;
                updateAmpInputHint();
                hideCpuTypeDropdown();
                resultText.setText("");
                digitalAmpDisplay.setText("0.0000 A");
                faultDisplayBox.setText("No Fault");
                faultDisplayBox.setTextColor(0xFF00FF00);
                errorDetailsLayout.removeAllViews();
                ampInput.setText("");
                chgAmpInput.setText("");
            });
            cpuTypeSelectionLayout.addView(tv);
        }

        // Position the dropdown near the button
        cpuTypeSelectionLayout.post(() -> {
            int[] location = new int[2];
            cpuTypeDropdownButton.getLocationOnScreen(location);
            int x = location[0] - cpuTypeSelectionLayout.getWidth() + cpuTypeDropdownButton.getWidth(); // Align right
            int y = location[1] + cpuTypeDropdownButton.getHeight() - dpToPx(200); // Adjust Y as needed

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cpuTypeSelectionLayout.getLayoutParams();
            params.leftMargin = x;
            params.topMargin = y;
            cpuTypeSelectionLayout.setLayoutParams(params);
        });
    }

    private void hideCpuTypeDropdown() {
        cpuTypeSelectionOverlay.setVisibility(View.GONE);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    // Inner classes for data
    private static class CFRValue {
        private double minAmp;
        private double maxAmp;
        private String name;

        public CFRValue(double minAmp, double maxAmp, String name) {
            this.minAmp = minAmp;
            this.maxAmp = maxAmp;
            this.name = name;
        }

        public boolean inRange(double amp) {
            return amp >= minAmp && amp <= maxAmp;
        }

        public String getName() {
            return name;
        }
    }

    private static class Behavior {
        private String cfrCode;
        private String englishDescription;
        private String myanmarDescription;
        private String checkPoint;
        private String solution;

        public Behavior(String cfrCode, String englishDescription, String myanmarDescription, String checkPoint, String solution) {
            this.cfrCode = cfrCode;
            this.englishDescription = englishDescription;
            this.myanmarDescription = myanmarDescription;
            this.checkPoint = checkPoint;
            this.solution = solution;
        }

        public String getCfrCode() {
            return cfrCode;
        }

        public String getEnglishDescription() {
            return englishDescription;
        }

        public String getMyanmarDescription() {
            return myanmarDescription;
        }

        public String getCheckPoint() {
            return checkPoint;
        }

        public String getSolution() {
            return solution;
        }
    }

    // Stop all simulations when activity is destroyed (if applicable, ensure this is needed)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stopAllSimulations(); // Uncomment if you have this method and need it
    }
}
