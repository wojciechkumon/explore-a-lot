//
//  DateViewController.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 14/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import UIKit
import McPicker

class DateViewController: UIViewController {
    @IBOutlet weak var startTextField: UITextField!
    @IBOutlet weak var finishTextField: UITextField!
    @IBOutlet weak var numberOfDaysTextField: UITextField!
    private var datePicker: UIDatePicker = UIDatePicker()
    
    private let contentService: ContentService = ContentService.shared
    private var startDate: Date!
    private var finishDate: Date!
    private var numberOfDays: Int = 7
    
    private var activeTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        startTextField.delegate = self
        finishTextField.delegate = self
        numberOfDaysTextField.delegate = self
        
        createDatePicker(forField: startTextField)
        createDatePicker(forField: finishTextField)
        
        startTextField.addTarget(self, action: #selector(touched(_:)), for: .touchDown)
        finishTextField.addTarget(self, action: #selector(touched(_:)), for: .touchDown)
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MMM-dd"
        startTextField.text = dateFormatter.string(from: Date())
        finishTextField.text = dateFormatter.string(from: Calendar.current.date(byAdding: .day, value: 7, to: Date())!)
    }
    
    func createDatePicker(forField field : UITextField){
        datePicker.datePickerMode = .date
        field.inputView = datePicker
        
        datePicker.addTarget(self, action: #selector(donePressed), for: .valueChanged)
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(viewTapped))
        view.addGestureRecognizer(tapGesture)
    }
    
    @objc func viewTapped(_ uiTapGesture: UITapGestureRecognizer) {
        view.endEditing(true)
    }
    
    @objc func donePressed(datePicker: UIDatePicker) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MMM-dd"
        let date: String = dateFormatter.string(from: datePicker.date)
        if activeTextField == startTextField {
            DispatchQueue.main.async {
                self.startTextField.text = date
                self.startDate = datePicker.date
            }
        } else if activeTextField == finishTextField {
            DispatchQueue.main.async {
                self.finishTextField.text = date
                self.finishDate = datePicker.date
            }
        }

        view.endEditing(true)
    }
    
    @objc func touched(_ textField: UITextField) {
        activeTextField = textField
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        contentService.startDate = self.startDate ?? Date()
        contentService.finishDate = self.finishDate ?? Calendar.current.date(byAdding: .day, value: 7, to: Date())!
        contentService.numberOfDays = self.numberOfDays
        super.prepare(for: segue, sender: sender)
    }
}

extension DateViewController: UITextFieldDelegate {
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == numberOfDaysTextField {
            guard let number = Int(textField.text!) else { return }
            self.numberOfDays = number
        }
    }
}
