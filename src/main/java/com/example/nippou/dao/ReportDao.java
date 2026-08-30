package com.example.nippou.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.nippou.model.Report;

/**
 * reportsテーブルに対するCRUD処理をまとめたDAOクラス。
 * SQLはすべて手書き(PreparedStatement)。フレームワークは使わない方針。
 */
public class ReportDao {

    /**
     * 日報を全件取得する。
     * 本来は「対象日(report_date)の降順」で並べたいが、
     * 実装ミスにより登録日時(created_at)の昇順になっている。
     */
    public List<Report> findAll() throws SQLException {
        String sql = "SELECT id, reporter_name, report_date, work_content, remarks, created_at, updated_at "
                + "FROM reports ORDER BY report_date DESC, id DESC";

        List<Report> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Report findById(int id) throws SQLException {
        String sql = "SELECT id, reporter_name, report_date, work_content, remarks, created_at, updated_at "
                + "FROM reports WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public void insert(Report report) throws SQLException {
        String sql = "INSERT INTO reports (reporter_name, report_date, work_content, remarks, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, report.getReporterName());
            stmt.setString(2, report.getReportDate());
            stmt.setString(3, report.getWorkContent());
            stmt.setString(4, report.getRemarks());
            stmt.executeUpdate();
        }
    }

    public void update(Report report) throws SQLException {
        String sql = "UPDATE reports SET reporter_name = ?, report_date = ?, work_content = ?, remarks = ?, updated_at = NOW() "
                + "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, report.getReporterName());
            stmt.setString(2, report.getReportDate());
            stmt.setString(3, report.getWorkContent());
            stmt.setString(4, report.getRemarks());
            stmt.setInt(5, report.getId());
            stmt.executeUpdate();
        }
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reports WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Report mapRow(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setId(rs.getInt("id"));
        report.setReporterName(rs.getString("reporter_name"));
        report.setReportDate(rs.getDate("report_date").toString());
        report.setWorkContent(rs.getString("work_content"));
        report.setRemarks(rs.getString("remarks"));
        report.setCreatedAt(rs.getTimestamp("created_at"));
        report.setUpdatedAt(rs.getTimestamp("updated_at"));
        return report;
    }
}
