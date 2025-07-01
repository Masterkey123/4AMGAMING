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
        mtkBehaviors.add(new Behavior("CFR1", "CPU VCORE LDO 1V2 POWER OK", "á€¡á€±á€¬á€€á€ºá€€ Power I C á€•á€»á€€á€º", "CPU IC", "CPU IC or Power IC"));
        mtkBehaviors.add(new Behavior("CFR2", "CPU LDO VOLTAGE DROP", "CPU LDO Voltage á€€á€»", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        mtkBehaviors.add(new Behavior("CFR3", "CPU CORE POWER FAULT", "CPU Core Power á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "CPU Power Line Test", "CPU IC or Power IC"));
        mtkBehaviors.add(new Behavior("CFR4", "CPU THERMAL SHUTDOWN", "CPU á€¡á€•á€°á€á€»á€­á€”á€ºá€œá€½á€”á€ºá€•á€¼á€®á€¸ á€•á€­á€á€ºá€žá€½á€¬á€¸", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        mtkBehaviors.add(new Behavior("CFR5", "CPU INTERNAL CLOCK FAULT", "CPU Clock á€¡á€œá€¯á€•á€ºá€™á€œá€¯á€•á€º", "CPU Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        mtkBehaviors.add(new Behavior("CFR6", "CPU BUS INTERFACE ERROR", "CPU Bus á€¡á€„á€ºá€á€¬á€–á€±á€·á€…á€º á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "CPU Bus Line Test", "CPU IC or Motherboard"));
        mtkBehaviors.add(new Behavior("CFR7", "CPU RESET FAILURE", "CPU Reset á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "CPU Reset Circuit Test", "CPU IC or Reset IC"));
        mtkBehaviors.add(new Behavior("CFR8", "CPU BOOTROM ERROR", "CPU Bootrom á€¡á€™á€¾á€¬á€¸", "CPU Bootrom Check", "CPU IC or EMMC/NAND"));
        mtkBehaviors.add(new Behavior("CFR9", "CPU EMI INTERFERENCE", "CPU EMI á€¡á€”á€¾á€±á€¬á€„á€·á€ºá€¡á€šá€¾á€€á€º", "EMI Shield Check", "Motherboard or EMI Shield"));
        mtkBehaviors.add(new Behavior("CFR10", "CPU POWER RAIL OVP", "CPU Power Rail Over Voltage", "Power Rail Voltage Check", "Power IC or PMIC"));
        mtkBehaviors.add(new Behavior("CFR11", "CPU MEMORY CONTROLLER ERROR", "CPU Memory Controller á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "Memory Interface Test", "CPU IC or RAM"));
        mtkBehaviors.add(new Behavior("CFR12", "CPU GFX POWER FAULT", "CPU GFX Power á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "GFX Power Line Test", "CPU IC or GPU IC"));
        mtkBehaviors.add(new Behavior("CFR13", "CPU SECURITY VIOLATION", "CPU á€œá€¯á€¶á€á€¼á€¯á€¶á€›á€±á€¸á€á€»á€­á€¯á€¸á€–á€±á€¬á€€á€ºá€™á€¾á€¯", "Security Feature Check", "CPU IC or Software Issue"));
        mtkBehaviors.add(new Behavior("CFR14", "CPU DAC/ADC ERROR", "CPU DAC/ADC á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "DAC/ADC Test", "CPU IC or Codec IC"));
        mtkBehaviors.add(new Behavior("CFR15", "CPU JTAG/DEBUG PORT ERROR", "CPU JTAG/Debug Port á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "JTAG Port Check", "CPU IC or Motherboard"));
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
        qualcommBehaviors.add(new Behavior("CFR1", "PMIC Output Fault", "PMIC á€¡á€‘á€½á€€á€ºá€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "PMIC Output Voltage Test", "PMIC or Power IC"));
        qualcommBehaviors.add(new Behavior("CFR2", "CPU LDO Undervoltage", "CPU LDO á€—á€­á€¯á€·á€¡á€¬á€¸á€€á€»", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        qualcommBehaviors.add(new Behavior("CFR3", "CPU Core Voltage Instability", "CPU Core Voltage á€™á€á€Šá€ºá€„á€¼á€­á€™á€º", "CPU Core Voltage Test", "CPU IC or Power IC"));
        qualcommBehaviors.add(new Behavior("CFR4", "CPU Overheating Protection", "CPU á€¡á€•á€°á€œá€½á€”á€ºá€€á€¬á€€á€½á€šá€ºá€™á€¾á€¯", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        qualcommBehaviors.add(new Behavior("CFR5", "Clock Generator Error", "Clock Generator á€¡á€™á€¾á€¬á€¸", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        qualcommBehaviors.add(new Behavior("CFR6", "Bus Communication Failure", "Bus á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "Bus Line Test", "CPU IC or Motherboard"));
        qualcommBehaviors.add(new Behavior("CFR7", "System Reset Loop", "System Reset á€–á€¼á€…á€ºá€”á€±", "Reset Circuit Test", "CPU IC or Reset IC"));
        qualcommBehaviors.add(new Behavior("CFR8", "Bootloader Corruption", "Bootloader á€•á€»á€€á€ºá€…á€®á€¸", "Bootloader Re-flash", "CPU IC or EMMC/NAND"));
        qualcommBehaviors.add(new Behavior("CFR9", "RF Interference on CPU Lines", "CPU Line á€á€½á€±á€™á€¾á€¬ RF á€¡á€”á€¾á€±á€¬á€„á€·á€ºá€¡á€šá€¾á€€á€º", "RF Shield Check", "Motherboard or RF Shield"));
        qualcommBehaviors.add(new Behavior("CFR10", "Power Rail Overcurrent", "Power Rail Over Current", "Power Rail Current Check", "Power IC or PMIC"));
        qualcommBehaviors.add(new Behavior("CFR11", "DDR Memory Error", "DDR Memory á€¡á€™á€¾á€¬á€¸", "Memory Interface Test", "CPU IC or RAM"));
        qualcommBehaviors.add(new Behavior("CFR12", "GPU Power Domain Issue", "GPU Power Domain á€•á€¼á€¿á€”á€¬", "GPU Power Line Test", "CPU IC or GPU IC"));
        qualcommBehaviors.add(new Behavior("CFR13", "Secure Boot Failure", "Secure Boot á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Secure Boot Check", "CPU IC or Software Issue"));
        qualcommBehaviors.add(new Behavior("CFR14", "Audio Codec Communication Error", "Audio Codec á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€¡á€™á€¾á€¬á€¸", "Audio Codec Test", "CPU IC or Codec IC"));
        qualcommBehaviors.add(new Behavior("CFR15", "Debug Interface Lockup", "Debug Interface Lock á€–á€¼á€…á€º", "Debug Port Check", "CPU IC or Motherboard"));
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
        huaweiBehaviors.add(new Behavior("CFR1", "HiSilicon PMU Fault", "HiSilicon PMU á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "PMU Voltage Test", "HiSilicon IC or PMU"));
        huaweiBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Fluctuation", "CPU LDO á€—á€­á€¯á€·á€¡á€¬á€¸á€¡á€á€€á€ºá€¡á€€á€»", "CPU LDO Voltage Measurement", "HiSilicon IC or PMU"));
        huaweiBehaviors.add(new Behavior("CFR3", "CPU Core Power Rail Sag", "CPU Core Power Rail á€€á€»á€†á€„á€ºá€¸", "CPU Core Voltage Test", "HiSilicon IC or PMU"));
        huaweiBehaviors.add(new Behavior("CFR4", "Thermal Throttling Engaged", "Thermal Throttling á€¡á€œá€¯á€•á€ºá€œá€¯á€•á€º", "CPU Thermal Sensor Test", "HiSilicon IC or Thermal Circuit"));
        huaweiBehaviors.add(new Behavior("CFR5", "System Clock Instability", "System Clock á€™á€á€Šá€ºá€„á€¼á€­á€™á€º", "Clock Circuit Test", "HiSilicon IC or Crystal Oscillator"));
        huaweiBehaviors.add(new Behavior("CFR6", "Inter-Processor Communication Error", "Processor á€™á€»á€¬á€¸á€¡á€€á€¼á€¬á€¸ á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€¡á€™á€¾á€¬á€¸", "IPC Bus Test", "HiSilicon IC or Motherboard"));
        huaweiBehaviors.add(new Behavior("CFR7", "Power-on Reset (POR) Loop", "Power-on Reset (POR) á€–á€¼á€…á€ºá€”á€±", "Reset Circuit Test", "HiSilicon IC or Reset IC"));
        huaweiBehaviors.add(new Behavior("CFR8", "eMMC/UFS Initialization Failure", "eMMC/UFS á€…á€á€„á€ºá€á€¼á€„á€ºá€¸ á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "eMMC/UFS Check", "HiSilicon IC or Storage IC"));
        huaweiBehaviors.add(new Behavior("CFR9", "Antenna or RF Interference", "Antenna á€žá€­á€¯á€·á€™á€Ÿá€¯á€á€º RF á€¡á€”á€¾á€±á€¬á€„á€·á€ºá€¡á€šá€¾á€€á€º", "Antenna/RF Shield Check", "Motherboard or RF Shield"));
        huaweiBehaviors.add(new Behavior("CFR10", "Battery Management Unit (BMU) Error", "Battery Management Unit (BMU) á€¡á€™á€¾á€¬á€¸", "Battery Voltage/Current Check", "BMU IC or Battery"));
        huaweiBehaviors.add(new Behavior("CFR11", "RAM Training Error", "RAM Training á€¡á€™á€¾á€¬á€¸", "Memory Interface Test", "HiSilicon IC or RAM"));
        huaweiBehaviors.add(new Behavior("CFR12", "NPU Power Domain Issue", "NPU Power Domain á€•á€¼á€¿á€”á€¬", "NPU Power Line Test", "HiSilicon IC or NPU"));
        huaweiBehaviors.add(new Behavior("CFR13", "Security Boot Chain Broken", "Security Boot Chain á€•á€»á€€á€ºá€…á€®á€¸", "Security Feature Check", "HiSilicon IC or Software Issue"));
        huaweiBehaviors.add(new Behavior("CFR14", "Audio DSP Communication Error", "Audio DSP á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€¡á€™á€¾á€¬á€¸", "Audio DSP Test", "HiSilicon IC or Codec IC"));
        huaweiBehaviors.add(new Behavior("CFR15", "Fastboot Mode Lock", "Fastboot Mode Lock á€–á€¼á€…á€º", "Fastboot Port Check", "HiSilicon IC or Motherboard"));
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
        samsungBehaviors.add(new Behavior("CFR1", "Exynos PMIC Output Error", "Exynos PMIC á€¡á€‘á€½á€€á€ºá€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "PMIC Output Voltage Test", "Exynos IC or PMIC"));
        samsungBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Drift", "CPU LDO á€—á€­á€¯á€·á€¡á€¬á€¸á€•á€¼á€±á€¬á€„á€ºá€¸á€œá€²", "CPU LDO Voltage Measurement", "Exynos IC or PMIC"));
        samsungBehaviors.add(new Behavior("CFR3", "CPU Core Power Rail Instability", "CPU Core Power Rail á€™á€á€Šá€ºá€„á€¼á€­á€™á€º", "CPU Core Voltage Test", "Exynos IC or PMIC"));
        samsungBehaviors.add(new Behavior("CFR4", "Thermal Sensor Malfunction", "Thermal Sensor á€¡á€œá€¯á€•á€ºá€™á€œá€¯á€•á€º", "CPU Thermal Sensor Test", "Exynos IC or Thermal Circuit"));
        samsungBehaviors.add(new Behavior("CFR5", "Clock Source Corruption", "Clock Source á€•á€»á€€á€ºá€…á€®á€¸", "Clock Circuit Test", "Exynos IC or Crystal Oscillator"));
        samsungBehaviors.add(new Behavior("CFR6", "Interconnect Bus Error", "Interconnect Bus á€¡á€™á€¾á€¬á€¸", "Bus Line Test", "Exynos IC or Motherboard"));
        samsungBehaviors.add(new Behavior("CFR7", "Watchdog Timer Reset", "Watchdog Timer Reset á€–á€¼á€…á€º", "Watchdog Circuit Test", "Exynos IC or Reset IC"));
        samsungBehaviors.add(new Behavior("CFR8", "UFS/eMMC Boot Failure", "UFS/eMMC Boot á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "UFS/eMMC Check", "Exynos IC or Storage IC"));
        samsungBehaviors.add(new Behavior("CFR9", "EMI Shielding Compromised", "EMI Shielding á€•á€»á€€á€ºá€…á€®á€¸", "EMI Shield Check", "Motherboard or EMI Shield"));
        samsungBehaviors.add(new Behavior("CFR10", "Fuel Gauge IC Error", "Fuel Gauge IC á€¡á€™á€¾á€¬á€¸", "Battery Fuel Gauge Check", "Fuel Gauge IC or Battery"));
        samsungBehaviors.add(new Behavior("CFR11", "DRAM Interface Error", "DRAM Interface á€¡á€™á€¾á€¬á€¸", "Memory Interface Test", "Exynos IC or RAM"));
        samsungBehaviors.add(new Behavior("CFR12", "ISP Power Domain Issue", "ISP Power Domain á€•á€¼á€¿á€”á€¬", "ISP Power Line Test", "Exynos IC or ISP"));
        samsungBehaviors.add(new Behavior("CFR13", "Knox Security Breach", "Knox Security á€á€»á€­á€¯á€¸á€–á€±á€¬á€€á€ºá€™á€¾á€¯", "Knox Feature Check", "Exynos IC or Software Issue"));
        samsungBehaviors.add(new Behavior("CFR14", "Audio Chip Communication Error", "Audio Chip á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€¡á€™á€¾á€¬á€¸", "Audio Chip Test", "Exynos IC or Codec IC"));
        samsungBehaviors.add(new Behavior("CFR15", "Download Mode Loop", "Download Mode Loop á€–á€¼á€…á€º", "Download Port Check", "Exynos IC or Motherboard"));
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
        lgBehaviors.add(new Behavior("CFR1", "Power Management IC (PMIC) Fault", "PMIC á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "PMIC Output Voltage Test", "PMIC or Power IC"));
        lgBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Instability", "CPU LDO á€—á€­á€¯á€·á€¡á€¬á€¸á€™á€á€Šá€ºá€„á€¼á€­á€™á€º", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        lgBehaviors.add(new Behavior("CFR3", "CPU Core Power Delivery Issue", "CPU Core Power á€•á€±á€¸á€•á€­á€¯á€·á€™á€¾á€¯ á€•á€¼á€¿á€”á€¬", "CPU Core Voltage Test", "CPU IC or Power IC"));
        lgBehaviors.add(new Behavior("CFR4", "Overheat Protection Activated", "á€¡á€•á€°á€œá€½á€”á€º á€€á€¬á€€á€½á€šá€ºá€™á€¾á€¯ á€¡á€œá€¯á€•á€ºá€œá€¯á€•á€º", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        lgBehaviors.add(new Behavior("CFR5", "Clock Signal Interruption", "Clock Signal á€•á€¼á€á€ºá€á€±á€¬á€€á€º", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        lgBehaviors.add(new Behavior("CFR6", "Data Bus Communication Error", "Data Bus á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€¡á€™á€¾á€¬á€¸", "Data Bus Line Test", "CPU IC or Motherboard"));
        lgBehaviors.add(new Behavior("CFR7", "System Reset Malfunction", "System Reset á€¡á€œá€¯á€•á€ºá€™á€œá€¯á€•á€º", "Reset Circuit Test", "CPU IC or Reset IC"));
        lgBehaviors.add(new Behavior("CFR8", "Boot Mode Entry Failure", "Boot Mode á€á€„á€ºá€›á€±á€¬á€€á€ºá€á€¼á€„á€ºá€¸ á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Boot Mode Check", "CPU IC or EMMC/NAND"));
        lgBehaviors.add(new Behavior("CFR9", "Electromagnetic Interference (EMI)", "á€œá€»á€¾á€•á€ºá€…á€…á€ºá€žá€¶á€œá€­á€¯á€€á€º á€”á€¾á€±á€¬á€„á€·á€ºá€šá€¾á€€á€ºá€™á€¾á€¯", "EMI Shield Check", "Motherboard or EMI Shield"));
        lgBehaviors.add(new Behavior("CFR10", "Battery Charging IC Error", "á€˜á€€á€ºá€‘á€›á€®á€¡á€¬á€¸á€žá€½á€„á€ºá€¸ IC á€¡á€™á€¾á€¬á€¸", "Charging IC Test", "Charging IC or Battery"));
        lgBehaviors.add(new Behavior("CFR11", "RAM Access Fault", "RAM Access á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "Memory Interface Test", "CPU IC or RAM"));
        lgBehaviors.add(new Behavior("CFR12", "Graphics Processing Unit (GPU) Power Issue", "GPU Power á€•á€¼á€¿á€”á€¬", "GPU Power Line Test", "CPU IC or GPU IC"));
        lgBehaviors.add(new Behavior("CFR13", "Firmware Integrity Check Failure", "Firmware Integrity Check á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Firmware Check", "CPU IC or Software Issue"));
        lgBehaviors.add(new Behavior("CFR14", "Audio Codec IC Malfunction", "Audio Codec IC á€¡á€œá€¯á€•á€ºá€™á€œá€¯á€•á€º", "Audio Codec Test", "CPU IC or Codec IC"));
        lgBehaviors.add(new Behavior("CFR15", "USB Debugging Port Error", "USB Debugging Port á€¡á€™á€¾á€¬á€¸", "USB Port Check", "CPU IC or Motherboard"));
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
        oppoBehaviors.add(new Behavior("CFR1", "OPPO Power IC Failure", "OPPO Power IC á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "Power IC Output Test", "Power IC"));
        oppoBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Drop", "CPU LDO Voltage á€€á€»", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        oppoBehaviors.add(new Behavior("CFR3", "CPU Core Power Fluctuation", "CPU Core Power á€™á€á€Šá€ºá€„á€¼á€­á€™á€º", "CPU Core Voltage Test", "CPU IC or Power IC"));
        oppoBehaviors.add(new Behavior("CFR4", "Thermal Protection Triggered", "Thermal Protection á€¡á€œá€¯á€•á€ºá€œá€¯á€•á€º", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        oppoBehaviors.add(new Behavior("CFR5", "Clock Source Error", "Clock Source á€¡á€™á€¾á€¬á€¸", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        oppoBehaviors.add(new Behavior("CFR6", "Data Bus Interruption", "Data Bus á€•á€¼á€á€ºá€á€±á€¬á€€á€º", "Data Bus Line Test", "CPU IC or Motherboard"));
        oppoBehaviors.add(new Behavior("CFR7", "System Reset Loop", "System Reset á€–á€¼á€…á€ºá€”á€±", "Reset Circuit Test", "CPU IC or Reset IC"));
        oppoBehaviors.add(new Behavior("CFR8", "Boot Partition Corruption", "Boot Partition á€•á€»á€€á€ºá€…á€®á€¸", "Boot Partition Check", "CPU IC or EMMC/NAND"));
        oppoBehaviors.add(new Behavior("CFR9", "RF Signal Interference", "RF Signal á€¡á€”á€¾á€±á€¬á€„á€·á€ºá€¡á€šá€¾á€€á€º", "RF Shield Check", "Motherboard or RF Shield"));
        oppoBehaviors.add(new Behavior("CFR10", "VOOC/SuperVOOC Charging Failure", "VOOC/SuperVOOC á€¡á€¬á€¸á€žá€½á€„á€ºá€¸ á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Charging IC Test", "Charging IC or Battery"));
        oppoBehaviors.add(new Behavior("CFR11", "RAM Data Integrity Error", "RAM Data Integrity á€¡á€™á€¾á€¬á€¸", "Memory Interface Test", "CPU IC or RAM"));
        oppoBehaviors.add(new Behavior("CFR12", "GPU Performance Issue", "GPU á€…á€½á€™á€ºá€¸á€†á€±á€¬á€„á€ºá€›á€Šá€º á€•á€¼á€¿á€”á€¬", "GPU Power Line Test", "CPU IC or GPU IC"));
        oppoBehaviors.add(new Behavior("CFR13", "ColorOS Security Error", "ColorOS á€œá€¯á€¶á€á€¼á€¯á€¶á€›á€±á€¸ á€¡á€™á€¾á€¬á€¸", "Security Feature Check", "CPU IC or Software Issue"));
        oppoBehaviors.add(new Behavior("CFR14", "Audio Processing Unit (APU) Error", "Audio Processing Unit (APU) á€¡á€™á€¾á€¬á€¸", "APU Test", "CPU IC or Codec IC"));
        oppoBehaviors.add(new Behavior("CFR15", "Recovery Mode Loop", "Recovery Mode Loop á€–á€¼á€…á€º", "Recovery Port Check", "CPU IC or Motherboard"));
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
        vivoBehaviors.add(new Behavior("CFR1", "Vivo Power Management IC (PMIC) Fault", "Vivo PMIC á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "PMIC Output Test", "PMIC"));
        vivoBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Fluctuation", "CPU LDO Voltage á€™á€á€Šá€ºá€„á€¼á€­á€™á€º", "CPU LDO Voltage Measurement", "CPU IC or PMIC"));
        vivoBehaviors.add(new Behavior("CFR3", "CPU Core Power Delivery Error", "CPU Core Power á€•á€±á€¸á€•á€­á€¯á€·á€™á€¾á€¯ á€¡á€™á€¾á€¬á€¸", "CPU Core Voltage Test", "CPU IC or PMIC"));
        vivoBehaviors.add(new Behavior("CFR4", "Thermal System Shutdown", "Thermal System á€•á€­á€á€ºá€žá€½á€¬á€¸", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        vivoBehaviors.add(new Behavior("CFR5", "Clock Generation Failure", "Clock Generation á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        vivoBehaviors.add(new Behavior("CFR6", "Interconnect Bus Error", "Interconnect Bus á€¡á€™á€¾á€¬á€¸", "Bus Line Test", "CPU IC or Motherboard"));
        vivoBehaviors.add(new Behavior("CFR7", "Software Reset Loop", "Software Reset á€–á€¼á€…á€ºá€”á€±", "Reset Circuit Test", "CPU IC or Software Issue"));
        vivoBehaviors.add(new Behavior("CFR8", "Bootloader Read/Write Error", "Bootloader á€–á€á€º/á€›á€±á€¸ á€¡á€™á€¾á€¬á€¸", "Bootloader Check", "CPU IC or EMMC/NAND"));
        vivoBehaviors.add(new Behavior("CFR9", "Wireless Interference", "Wireless á€¡á€”á€¾á€±á€¬á€„á€·á€ºá€¡á€šá€¾á€€á€º", "Wireless Shield Check", "Motherboard or Wireless Module"));
        vivoBehaviors.add(new Behavior("CFR10", "FlashCharge/SuperFlashCharge Failure", "FlashCharge/SuperFlashCharge á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Charging IC Test", "Charging IC or Battery"));
        vivoBehaviors.add(new Behavior("CFR11", "RAM ECC Error", "RAM ECC á€¡á€™á€¾á€¬á€¸", "Memory Interface Test", "CPU IC or RAM"));
        vivoBehaviors.add(new Behavior("CFR12", "DSP Power Domain Issue", "DSP Power Domain á€•á€¼á€¿á€”á€¬", "DSP Power Line Test", "CPU IC or DSP"));
        vivoBehaviors.add(new Behavior("CFR13", "Funtouch OS Security Breach", "Funtouch OS á€œá€¯á€¶á€á€¼á€¯á€¶á€›á€±á€¸ á€á€»á€­á€¯á€¸á€–á€±á€¬á€€á€ºá€™á€¾á€¯", "Security Feature Check", "CPU IC or Software Issue"));
        vivoBehaviors.add(new Behavior("CFR14", "Audio Codec Interruption", "Audio Codec á€•á€¼á€á€ºá€á€±á€¬á€€á€º", "Audio Codec Test", "CPU IC or Codec IC"));
        vivoBehaviors.add(new Behavior("CFR15", "Service Mode Lock", "Service Mode Lock á€–á€¼á€…á€º", "Service Port Check", "CPU IC or Motherboard"));
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
        xiaomiBehaviors.add(new Behavior("CFR1", "Xiaomi Power Management IC Fault", "Xiaomi Power IC á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "Power IC Output Test", "Power IC"));
        xiaomiBehaviors.add(new Behavior("CFR2", "CPU LDO Voltage Drop", "CPU LDO Voltage á€€á€»", "CPU LDO Voltage Measurement", "CPU IC or Power IC"));
        xiaomiBehaviors.add(new Behavior("CFR3", "CPU Core Power Irregularity", "CPU Core Power á€•á€¯á€¶á€™á€™á€¾á€”á€º", "CPU Core Voltage Test", "CPU IC or Power IC"));
        xiaomiBehaviors.add(new Behavior("CFR4", "Thermal Shutdown Activated", "Thermal Shutdown á€¡á€œá€¯á€•á€ºá€œá€¯á€•á€º", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        xiaomiBehaviors.add(new Behavior("CFR5", "Clock Signal Glitch", "Clock Signal á€•á€¼á€¿á€”á€¬", "Clock Circuit Test", "CPU IC or Crystal Oscillator"));
        xiaomiBehaviors.add(new Behavior("CFR6", "System Bus Error", "System Bus á€¡á€™á€¾á€¬á€¸", "Bus Line Test", "CPU IC or Motherboard"));
        xiaomiBehaviors.add(new Behavior("CFR7", "Hardware Reset Loop", "Hardware Reset á€–á€¼á€…á€ºá€”á€±", "Reset Circuit Test", "CPU IC or Reset IC"));
        xiaomiBehaviors.add(new Behavior("CFR8", "MIUI Boot Failure", "MIUI Boot á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Bootloader Check", "CPU IC or EMMC/NAND"));
        xiaomiBehaviors.add(new Behavior("CFR9", "Network Interference", "Network á€¡á€”á€¾á€±á€¬á€„á€·á€ºá€¡á€šá€¾á€€á€º", "Network Shield Check", "Motherboard or Network Module"));
        xiaomiBehaviors.add(new Behavior("CFR10", "Mi Charge/HyperCharge Failure", "Mi Charge/HyperCharge á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Charging IC Test", "Charging IC or Battery"));
        xiaomiBehaviors.add(new Behavior("CFR11", "Memory Controller Error", "Memory Controller á€¡á€™á€¾á€¬á€¸", "Memory Interface Test", "CPU IC or RAM"));
        xiaomiBehaviors.add(new Behavior("CFR12", "Image Signal Processor (ISP) Power Issue", "ISP Power á€•á€¼á€¿á€”á€¬", "ISP Power Line Test", "CPU IC or ISP"));
        xiaomiBehaviors.add(new Behavior("CFR13", "MIUI Security Feature Disabled", "MIUI á€œá€¯á€¶á€á€¼á€¯á€¶á€›á€±á€¸ feature á€•á€­á€á€º", "Security Feature Check", "CPU IC or Software Issue"));
        xiaomiBehaviors.add(new Behavior("CFR14", "Audio Codec Communication Fault", "Audio Codec á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "Audio Codec Test", "CPU IC or Codec IC"));
        xiaomiBehaviors.add(new Behavior("CFR15", "EDL Mode Lock", "EDL Mode Lock á€–á€¼á€…á€º", "EDL Port Check", "CPU IC or Motherboard"));
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
        appleBehaviors.add(new Behavior("CFR1", "Apple PMIC Output Failure", "Apple PMIC á€¡á€‘á€½á€€á€ºá€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "PMIC Output Test", "PMIC"));
        appleBehaviors.add(new Behavior("CFR2", "CPU Voltage Regulator Module (VRM) Issue", "CPU VRM á€•á€¼á€¿á€”á€¬", "CPU VRM Voltage Measurement", "CPU IC or VRM"));
        appleBehaviors.add(new Behavior("CFR3", "CPU Core Power Rail Sag", "CPU Core Power Rail á€€á€»á€†á€„á€ºá€¸", "CPU Core Voltage Test", "CPU IC or PMIC"));
        appleBehaviors.add(new Behavior("CFR4", "Thermal Management System Fault", "Thermal Management System á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "CPU Thermal Sensor Test", "CPU IC or Thermal Circuit"));
        appleBehaviors.add(new Behavior("CFR5", "System Clock Synchronization Error", "System Clock Synchronization á€¡á€™á€¾á€¬á€¸", "Clock Circuit Test", "CPU IC or Clock Generator"));
        appleBehaviors.add(new Behavior("CFR6", "Inter-Processor Communication (IPC) Failure", "IPC á€á€»á€½á€á€ºá€šá€½á€„á€ºá€¸", "IPC Bus Test", "CPU IC or Motherboard"));
        appleBehaviors.add(new Behavior("CFR7", "Hardware Reset Loop", "Hardware Reset á€–á€¼á€…á€ºá€”á€±", "Reset Circuit Test", "CPU IC or Reset IC"));
        appleBehaviors.add(new Behavior("CFR8", "Secure Enclave Processor (SEP) Error", "SEP á€¡á€™á€¾á€¬á€¸", "SEP Check", "CPU IC or SEP Module"));
        appleBehaviors.add(new Behavior("CFR9", "RF/Cellular Interference", "RF/Cellular á€¡á€”á€¾á€±á€¬á€„á€·á€ºá€¡á€šá€¾á€€á€º", "RF Shield Check", "Motherboard or RF Module"));
        appleBehaviors.add(new Behavior("CFR10", "Charging IC/Tristar Issue", "Charging IC/Tristar á€•á€¼á€¿á€”á€¬", "Charging IC Test", "Charging IC or Battery"));
        appleBehaviors.add(new Behavior("CFR11", "RAM/NAND Interface Error", "RAM/NAND Interface á€¡á€™á€¾á€¬á€¸", "Memory Interface Test", "CPU IC or Memory IC"));
        appleBehaviors.add(new Behavior("CFR12", "GPU Power Draw Anomaly", "GPU Power á€†á€½á€²á€¡á€¬á€¸ á€•á€¯á€¶á€™á€™á€¾á€”á€º", "GPU Power Line Test", "CPU IC or GPU IC"));
        appleBehaviors.add(new Behavior("CFR13", "Face ID/Touch ID Security Error", "Face ID/Touch ID á€œá€¯á€¶á€á€¼á€¯á€¶á€›á€±á€¸ á€¡á€™á€¾á€¬á€¸", "Security Feature Check", "CPU IC or Biometric Module"));
        appleBehaviors.add(new Behavior("CFR14", "Audio IC/Codec Communication Failure", "Audio IC/Codec á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€™á€¡á€±á€¬á€„á€ºá€™á€¼á€„á€º", "Audio IC Test", "CPU IC or Audio IC"));
        appleBehaviors.add(new Behavior("CFR15", "DFU Mode Lock", "DFU Mode Lock á€–á€¼á€…á€º", "DFU Port Check", "CPU IC or Motherboard"));
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
        chgAmpBehaviors.add(new Behavior("ChgCFR1", "Battery Temperature High", "á€˜á€€á€ºá€‘á€›á€® á€¡á€•á€°á€á€»á€­á€”á€ºá€œá€½á€”á€º", "á€˜á€€á€ºá€‘á€›á€®/NTC á€¡á€¬á€›á€¯á€¶á€á€¶á€…á€”á€…á€º á€…á€…á€ºá€†á€±á€¸á€•á€«", "á€˜á€€á€ºá€‘á€›á€® á€žá€­á€¯á€·á€™á€Ÿá€¯á€á€º NTC á€¡á€•á€°á€á€»á€­á€”á€ºá€‘á€­á€”á€ºá€¸á€á€»á€¯á€•á€ºá€™á€¾á€¯"));
        chgAmpBehaviors.add(new Behavior("ChgCFR2", "Charging IC Fault", "á€¡á€¬á€¸á€žá€½á€„á€ºá€¸ IC á€•á€»á€€á€º", "á€¡á€¬á€¸á€žá€½á€„á€ºá€¸ IC á€—á€­á€¯á€·á€¡á€¬á€¸áŠ á€¡á€‘á€½á€€á€ºáŠ ground á€á€½á€± á€…á€…á€ºá€†á€±á€¸á€•á€«", "Charging IC"));
        chgAmpBehaviors.add(new Behavior("ChgCFR3", "USB Data Line Fault", "USB Data Line á€•á€»á€€á€º", "USB data line (D+, D-) á€á€½á€± á€…á€…á€ºá€†á€±á€¸á€•á€«", "USB Port or Data Line Circuit"));
        chgAmpBehaviors.add(new Behavior("ChgCFR4", "Battery Connector Issue", "á€˜á€€á€ºá€‘á€›á€® connector á€•á€¼á€¿á€”á€¬", "á€˜á€€á€ºá€‘á€›á€® connector á€”á€²á€· pin á€á€½á€± á€…á€…á€ºá€†á€±á€¸á€•á€«", "Battery Connector"));
        chgAmpBehaviors.add(new Behavior("ChgCFR5", "Power Supply Low", "Power Supply á€¡á€¬á€¸á€”á€Šá€ºá€¸", "Adapter/charger á€—á€­á€¯á€·á€¡á€¬á€¸á€”á€²á€· current á€…á€…á€ºá€†á€±á€¸á€•á€«", "Charger/Power Adapter"));
        chgAmpBehaviors.add(new Behavior("ChgCFR6", "Overvoltage Protection Active", "á€—á€­á€¯á€·á€¡á€¬á€¸á€œá€½á€”á€º á€€á€¬á€€á€½á€šá€ºá€™á€¾á€¯ á€¡á€œá€¯á€•á€ºá€œá€¯á€•á€º", "á€—á€­á€¯á€·á€¡á€¬á€¸á€œá€½á€”á€º á€€á€¬á€€á€½á€šá€ºá€™á€¾á€¯ (OVP) IC á€€á€­á€¯ á€…á€…á€ºá€†á€±á€¸á€•á€«", "OVP IC or PMIC"));
        chgAmpBehaviors.add(new Behavior("ChgCFR7", "Charging Port Damage", "á€¡á€¬á€¸á€žá€½á€„á€ºá€¸á€•á€±á€«á€€á€º á€•á€»á€€á€ºá€…á€®á€¸", "á€¡á€¬á€¸á€žá€½á€„á€ºá€¸á€•á€±á€«á€€á€º (USB port) á€•á€»á€€á€ºá€…á€®á€¸á€™á€¾á€¯ á€…á€…á€ºá€†á€±á€¸á€•á€«", "Charging Port"));
        chgAmpBehaviors.add(new Behavior("ChgCFR8", "Software Charging Error", "á€†á€±á€¬á€·á€–á€ºá€á€²á€œá€º á€¡á€¬á€¸á€žá€½á€„á€ºá€¸á€™á€¾á€¯ á€¡á€™á€¾á€¬á€¸", "Firmware/Software á€€á€­á€¯ Update á€’á€«á€™á€¾á€™á€Ÿá€¯á€á€º Reset á€œá€¯á€•á€ºá€•á€«", "Software Issue"));
        chgAmpBehaviors.add(new Behavior("ChgCFR9", "Charge Pump Fault", "Charge Pump á€•á€»á€€á€º", "Charge Pump IC á€€á€­á€¯ á€…á€…á€ºá€†á€±á€¸á€•á€«", "Charge Pump IC"));
        chgAmpBehaviors.add(new Behavior("ChgCFR10", "Thermal Pad Issue", "Thermal Pad á€•á€¼á€¿á€”á€¬", "Thermal pad á€á€½á€± á€™á€¾á€”á€ºá€€á€”á€ºá€…á€½á€¬ á€‘á€­á€á€½á€±á€·á€”á€±á€™á€¾á€¯ á€…á€…á€ºá€†á€±á€¸á€•á€«", "Thermal Pad or Heatsink"));
        chgAmpBehaviors.add(new Behavior("ChgCFR11", "Auxiliary Charging Path Fault", "á€¡á€›á€”á€º á€¡á€¬á€¸á€žá€½á€„á€ºá€¸á€œá€™á€ºá€¸á€€á€¼á€±á€¬á€„á€ºá€¸ á€•á€»á€€á€º", "á€¡á€›á€”á€ºá€¡á€¬á€¸á€žá€½á€„á€ºá€¸á€œá€™á€ºá€¸á€€á€¼á€±á€¬á€„á€ºá€¸á€€á€­á€¯ á€…á€…á€ºá€†á€±á€¸á€•á€«", "Auxiliary Charging Circuit"));
        chgAmpBehaviors.add(new Behavior("ChgCFR12", "Charger Not Detected", "á€¡á€¬á€¸á€žá€½á€„á€ºá€¸á€€á€­á€›á€­á€šá€¬ á€›á€¾á€¬á€™á€á€½á€±á€·", "Charger á€”á€²á€· Cable á€€á€­á€¯ á€…á€…á€ºá€†á€±á€¸á€•á€«", "Charger or USB Cable"));
        chgAmpBehaviors.add(new Behavior("ChgCFR13", "Charging Communication Error", "á€¡á€¬á€¸á€žá€½á€„á€ºá€¸ á€†á€€á€ºá€žá€½á€šá€ºá€™á€¾á€¯ á€¡á€™á€¾á€¬á€¸", "Communication lines (e.g., CC, DPM) á€€á€­á€¯ á€…á€…á€ºá€†á€±á€¸á€•á€«", "Charging IC or Communication Lines"));
        chgAmpBehaviors.add(new Behavior("ChgCFR14", "High Resistance in Charging Path", "á€¡á€¬á€¸á€žá€½á€„á€ºá€¸á€œá€™á€ºá€¸á€€á€¼á€±á€¬á€„á€ºá€¸á€™á€¾á€¬ á€á€¶á€”á€­á€¯á€„á€ºá€›á€Šá€ºá€™á€¼á€„á€·á€ºá€™á€¬á€¸", "Charging path á€™á€¾á€¬á€›á€¾á€­á€á€²á€· Resistor á€á€½á€±áŠ Cable á€á€½á€± á€…á€…á€ºá€†á€±á€¸á€•á€«", "Resistors or Cable"));
        chgAmpBehaviors.add(new Behavior("ChgCFR15", "Battery ID / NTC Fault", "á€˜á€€á€ºá€‘á€›á€® ID / NTC á€•á€»á€€á€º", "á€˜á€€á€ºá€‘á€›á€® ID pin á€”á€²á€· NTC pin á€á€½á€± á€…á€…á€ºá€†á€±á€¸á€•á€«", "Battery or NTC Circuit"));
        cpuBehaviors.put("Chg Amp", chgAmpBehaviors);
    }

    private void updateAmpInputHint() {
        if ("Chg Amp".equals(selectedCpuType)) {
            ampInput.setHint("Amp á€€á€­á€¯ Chg Amp á€‘á€²á€€á€šá€°á€•á€«");
            chgAmpSection.setVisibility(View.VISIBLE);
            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(dpToPx(8));
            background.setColor(0xFF333333);
            background.setStroke(dpToPx(1), 0xFFE0C240); // Orange border
            chgAmpInput.setBackground(background);
        } else {
            ampInput.setHint("AMP á€›á€±á€¸á€•á€«");
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
            Toast.makeText(this, "AMP á€á€”á€ºá€–á€­á€¯á€¸ á€‘á€Šá€·á€ºá€•á€«", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "á€™á€¾á€”á€ºá€€á€”á€ºá€žá€±á€¬ AMP á€á€”á€ºá€–á€­á€¯á€¸ á€‘á€Šá€·á€ºá€•á€«", Toast.LENGTH_SHORT).show();
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

/*
 * === CFR8 Boot Logo Stuck Diagnostic Summary ===
 *
 * Brand    : Huawei
 * CFR Code : CFR8
 * Fault    : eMMC/UFS Initialization Failure
 * Check    : eMMC/UFS Check
 * Solution : HiSilicon IC or Storage IC
 *
 * Brand    : Samsung
 * CFR Code : CFR8
 * Fault    : UFS/eMMC Boot Failure
 * Check    : UFS/eMMC Check
 * Solution : Exynos IC or Storage IC
 *
 * Brand    : Vivo
 * CFR Code : CFR8
 * Fault    : Bootloader Read/Write Error
 * Check    : Bootloader Check
 * Solution : CPU IC or EMMC/NAND
 *
 * Brand    : Oppo
 * CFR Code : CFR8
 * Fault    : Boot Partition Corruption
 * Check    : Boot Partition Check
 * Solution : CPU IC or EMMC/NAND
 *
 * Brand    : Xiaomi
 * CFR Code : CFR8
 * Fault    : MIUI Boot Failure
 * Check    : Bootloader Check
 * Solution : CPU IC or EMMC/NAND
 */
